package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.GrandPrix
import ru.project.f1.service.GrandPrixService
import ru.project.f1.view.dialog.GrandPrixDialog
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.reflect.KMutableProperty1

@Route("grand-prix")
@Component
@PreserveOnRefresh
@UIScope
@PageTitle("F1 | Grand Prix")
class GrandPrixListView : HasImage() {

    @Autowired
    private lateinit var grandPrixService: GrandPrixService

    @Autowired
    private lateinit var grandPrixDialog: GrandPrixDialog
    private lateinit var grid: Grid<GrandPrix>
    private val createdDateRef: KMutableProperty1<GrandPrix, LocalDate> = GrandPrix::date
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    private val renderer = LocalDateRenderer(createdDateRef, formatter)
    private lateinit var select: Select<String>

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                horizontalLayout {
                    style.set("margin-top", "0px")
                    setWidthFull()
                    title("Grand Prix")
                    button("Add Grand Prix") {
                        onLeftClick {
                            grandPrixDialog.openAndThen {
                                grid.setItems(grandPrixService.findAllGrandPrixByYear(2021))
                            }
                        }
                    }
                    select = select {
                        label = "Year"
                    }
                }
                grid = grid {
                    setSelectionMode(Grid.SelectionMode.NONE)
                    isAllRowsVisible = true
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        val grandPrixList = grandPrixService.findAllGrandPrixByYear(2021)
        grid.apply {
            removeAllColumns()
            addComponentColumn {
                val grandPrix =
                    grandPrixService.findGrandPrixById(it.id).orElseThrow()
                val country = grandPrix.track.country
                val image = imageById(country.f1File.id, it.fullName) {
                    height = "20px"
                    width = "30px"
                }
                val text = Span(it.fullName).apply {
                    style.apply {
                        set("margin-left", "10px")
                        set("align-self", "center")
                    }
                }
                Anchor("/grand-prix/${it.id}", image, text).apply {
                    style.set("display", "inline-flex")
                }
            }
            columnFor(createdDateRef, renderer) {
                isSortable = false
                textAlign = ColumnTextAlign.END
                setHeader("")
            }
            setItems(grandPrixList)
            addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES)
        }

        select.apply {
            val years = grandPrixService.findAllYears().map { it.toString() }
            setItems(years)
            value = years.last()
        }
    }
}