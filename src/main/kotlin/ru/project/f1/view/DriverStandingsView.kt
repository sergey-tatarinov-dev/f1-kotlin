package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.orderedlayout.FlexComponent
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
import java.text.NumberFormat
import kotlin.reflect.KProperty1


@Route("driver-standings")
@Component
@PageTitle("F1 | Driver tandings")
@PreserveOnRefresh
@UIScope
class DriverStandingsView : StandingView() {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

    @Autowired
    private lateinit var driverStandingsService: DriverStandingsService

    private lateinit var driverStandings: List<DriverStanding>
    private lateinit var grid: Grid<DriverStanding>
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
                width = "65%"
                style.set("margin-top", "0px")
                horizontalLayout {
                    style.set("margin-top", "0px")
                    setWidthFull()
                    h1("Drivers standings") {
                        style.set("flex-grow", "1")
                    }
                    select<String> {
                        label = "Year"
                        setItems("2019", "2020", "2021")
                        value = "2021"
                    }
                }
                grid = grid {
                    isAllRowsVisible = true
                    setSelectionMode(Grid.SelectionMode.NONE)
                    addThemeVariants(
                        GridVariant.LUMO_NO_BORDER,
                        GridVariant.LUMO_NO_ROW_BORDERS,
                        GridVariant.LUMO_ROW_STRIPES
                    )
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
                        text = driverStanding.name
                        href = "/driver/${driverStanding.id}"
                    }
                }).setHeader("Name")
                addColumnFor(DriverStanding::sum, renderer.invoke(DriverStanding::sum))
            }
        }
    }
}