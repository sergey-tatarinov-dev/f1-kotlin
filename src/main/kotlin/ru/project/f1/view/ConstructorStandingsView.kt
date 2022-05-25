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
import ru.project.f1.entity.TeamStanding
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.service.TeamStandingsService
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import java.text.NumberFormat

@Route("constructor-standings")
@Component
@PageTitle("F1 | Constructor standings")
@PreserveOnRefresh
@UIScope
class ConstructorStandingsView : StandingView() {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

    @Autowired
    private lateinit var teamStandingsService: TeamStandingsService

    private lateinit var teamStandings: List<TeamStanding>
    private lateinit var grid: Grid<TeamStanding>
    private lateinit var select: Select<String>

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
                    h1("Constructors standings") {
                        style.set("flex-grow", "1")
                    }
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
        teamStandings = teamStandingsService.findAll(PageRequest.of(0, 25)).toMutableList()
        grid.apply {
            removeAllColumns()
            setItems(teamStandings)
            addColumns {
                addColumn(ComponentRenderer(::Anchor) { anchor: Anchor, teamStanding: TeamStanding ->
                    anchor.apply {
                        text = teamStanding.name
                        href = "/team/${teamStanding.id}"
                    }
                }).setHeader("Name")
                columnFor(
                    TeamStanding::sum,
                    NumberRenderer(TeamStanding::sum, NumberFormat.getNumberInstance())
                )
            }
        }
        select.apply {
            val years = grandPrixResultService.findAllYears().map { it.toString() }
            setItems(years)
            value = years.last()
        }
    }
}