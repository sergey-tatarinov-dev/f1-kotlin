package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.ColumnTextAlign
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
import ru.project.f1.entity.GrandPrixResult
import ru.project.f1.entity.TeamStanding
import ru.project.f1.service.FileService
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

    @Autowired
    private lateinit var fileService: FileService

    private lateinit var teamStandings: List<TeamStanding>
    private lateinit var grid: Grid<TeamStanding>
    private lateinit var grandPrixResults: MutableList<GrandPrixResult>

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
                    select<String> {
                        label = "Year"
                        value = "2021"
                    }.setItems("2019", "2020", "2021")
                }
                grid = grid {
                    isHeightByRows = true
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
        grandPrixResults = grandPrixResultService.findAll(PageRequest.of(0, 400)).toMutableList()
        teamStandings = teamStandingsService.findAll(PageRequest.of(0, 25)).toMutableList()
        grid.removeAllColumns()
        grid.setItems(teamStandings)
        grid.addColumn(ComponentRenderer(::Anchor) { anchor: Anchor, teamStanding: TeamStanding ->
            anchor.apply {
                text = teamStanding.name
                href = "/team/${teamStanding.id}"
            }
        }).setHeader("Name")
        grid.addColumnFor(
            TeamStanding::sum,
            NumberRenderer(TeamStanding::sum, NumberFormat.getNumberInstance())
        ).apply {
            isSortable = false
            width = "2%"
            textAlign = ColumnTextAlign.CENTER
            grid.style.set("font-size", "13px")
        }
        grandPrixResults
            .sortedBy { it.grandPrix.date }
            .groupBy { it.grandPrix }
            .toList()
            .forEachIndexed { index, standing ->
                run {
                    grid.addColumn(
                        NumberRenderer(
                            {
                                it.points[index]
                            }, NumberFormat.getNumberInstance()
                        )
                    ).apply {
                        width = "1%"
                        textAlign = ColumnTextAlign.CENTER

                        val findById = fileService.findById(standing.first.track.country.id)
                        val src = findById.orElse(null).absolutePath
                        val alt = standing.first.fullName
                        setHeader(Anchor("/grand-prix/${standing.first.id}", imageFromPath(src, alt))
                            .apply { setTitle(standing.first.fullName) }
                        )
                    }
                }
            }
    }


}