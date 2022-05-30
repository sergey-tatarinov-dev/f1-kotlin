package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.DriverStanding
import ru.project.f1.service.DriverStandingsService
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title
import java.text.NumberFormat
import kotlin.reflect.KProperty1


@Route("drivers-standings")
@Component
@PageTitle("F1 | Drivers standings")
@PreserveOnRefresh
@UIScope
class DriverStandingsView : StandingView() {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

    @Autowired
    private lateinit var driverStandingsService: DriverStandingsService

    private lateinit var driverStandings: List<DriverStanding>
    private lateinit var grid: Grid<DriverStanding>
    private lateinit var select: Select<String>

    private var renderer = { valueProvider: KProperty1<DriverStanding, Double> ->
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
                width = "85%"
                style.set("margin-top", "0px")
                horizontalLayout {
                    style.set("margin-top", "0px")
                    setWidthFull()
                    title("Drivers standings")
                    select = select {
                        label = "Year"
                    }
                }
                grid = grid {
                    isAllRowsVisible = true
                    setSelectionMode(Grid.SelectionMode.NONE)
                    addThemeVariants(LUMO_NO_BORDER, LUMO_NO_ROW_BORDERS, LUMO_ROW_STRIPES)
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        grandPrixResultService.createViews()
        driverStandings = driverStandingsService.findAll(PageRequest.of(0, 25)).toMutableList()
        grid.apply {
            removeAllColumns()
            setItems(driverStandings)
            addColumns {
                addColumn(ComponentRenderer(::Anchor) { anchor: Anchor, driverStanding: DriverStanding ->
                    anchor.apply {
                        val split = driverStanding.name.split(" ")
                        text = "${split[0]} ${split[1].uppercase()}"
                        href = "/driver/${driverStanding.id}"
                    }
                }).setHeader("Name")
                columnFor(DriverStanding::sum, renderer.invoke(DriverStanding::sum))
            }
        }
        select.apply {
            val years = grandPrixResultService.findAllYears().map { it.toString() }
            setItems(years)
            value = years.last()
        }
    }
}