package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.GrandPrixResultPerDriver
import ru.project.f1.service.DriverService
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.service.GrandPrixService
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title
import java.text.NumberFormat
import kotlin.reflect.KProperty1

@Route("driver/:id")
@Component
@PreserveOnRefresh
@UIScope
class DriverView : BeforeEnterObserver, HasDynamicTitle, HasImage() {

    @Autowired
    private lateinit var driverService: DriverService

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService
    @Autowired
    private lateinit var grandPrixService: GrandPrixService
    private lateinit var titleLayout: HorizontalLayout
    private lateinit var grid: Grid<GrandPrixResultPerDriver>
    private var yearSelect: Select<String> = Select()
    private lateinit var driverId: String
    private lateinit var pageTitle: String

    private var renderer = { valueProvider: KProperty1<GrandPrixResultPerDriver, Double> ->
        NumberRenderer(
            valueProvider,
            NumberFormat.getNumberInstance()
        )
    }

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                style.set("margin-top", "0px")
                horizontalLayout {
                    style.set("margin-top", "0px")
                    setWidthFull()
                    title("Constructors standings")
                }
                titleLayout = horizontalLayout {}
                grid = grid {
                    setSelectionMode(Grid.SelectionMode.NONE)
                    isAllRowsVisible = true
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)

        val years = grandPrixService.findAllYears().map { it.toString() }

        yearSelect.apply {
            setItems(years)
            label = "Year"
            value = years.last()
            addValueChangeListener {
                if (it != null && it.value != null) {
                    updateView(it.value.toInt())
                }
            }
        }

        val driver = driverService.findById(driverId.toBigInteger()).orElseThrow()
        pageTitle = "${driver.name} ${driver.surname.uppercase()}"
        titleLayout.apply {
            style.set("margin-top", "0px")
            setWidthFull()
            removeAll()
            setWidthFull()
            add(
                imageById(driver.country.f1File.id, "${driver.name} ${driver.surname}") {
                    height = "30px"
                    width = "45px"
                },
                title(pageTitle),
                yearSelect
            )
        }

        updateView(yearSelect.value.toInt())
        grid.apply {
            removeAllColumns()
            addComponentColumn {
                val grandPrix =
                    grandPrixService.findGrandPrixById(it.grandPrixId).orElseThrow()
                pageTitle = "${grandPrix.fullName} ${grandPrix.date.year}"
                val country = grandPrix.track.country
                val image = imageById(country.f1File.id, it.grandPrixName) {
                    height = "20px"
                    width = "30px"
                }
                val text = Span(it.grandPrixName).apply {
                    style.apply {
                        set("margin-left", "10px")
                        set("align-self", "center")
                    }
                }
                Anchor("/grand-prix/${it.grandPrixId}", image, text).apply {
                    style.set("display", "inline-flex")
                }
            }.apply {
                setHeader("Grand Prix")
            }
            columnFor(GrandPrixResultPerDriver::points, renderer.invoke(GrandPrixResultPerDriver::points)) {
                isSortable = false
                textAlign = ColumnTextAlign.END
            }
            addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES)
        }
    }

    fun updateView(year: Int) {
        val grandPrixList = grandPrixResultService.findAllByDriverId(driverId.toInt(), year)
        grid.apply {
            setItems(grandPrixList)
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        driverId = event!!.routeParameters["id"].get()
    }

    override fun getPageTitle(): String = pageTitle
}