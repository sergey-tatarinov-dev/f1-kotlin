package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.dto.LineUpDto
import ru.project.f1.dto.PointsPerGrandPrixDto
import ru.project.f1.dto.TeamStandingDto
import ru.project.f1.service.*
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title

@Route("constructor-standings")
@Component
@PageTitle("F1 | Constructor standings")
@PreserveOnRefresh
@UIScope
class ConstructorStandingsView : AbstractStandingView<TeamStandingDto>() {

    @Autowired
    private lateinit var driverService: DriverService

    @Autowired
    private lateinit var teamService: TeamService

    override lateinit var grid: Grid<TeamStandingDto>
    override val renderer: ComponentRenderer<Anchor, TeamStandingDto>
        get() = ComponentRenderer(::Anchor) { anchor: Anchor, teamStandingDto: TeamStandingDto ->
            anchor.apply {
                text = teamStandingDto.team.name
                href = "/team/${teamStandingDto.team.id}"
            }
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
                    title("Constructors standings")
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

    override fun addPointsColumns(year: Int) {
        getTeamGrandPrixResultDto(year)[0].grandPrixPoints.forEachIndexed { id, pointsPerGrandPrixDto ->
            addPointsValueToGrid(id, pointsPerGrandPrixDto)
        }
    }

    override fun getItems(year: Int): List<TeamStandingDto> = getTeamGrandPrixResultDto(year)

    private fun getTeamGrandPrixResultDto(year: Int): List<TeamStandingDto> = driverService.findAllLineUpByYear(year)
        .asSequence()
        .map { itt ->
            LineUpDto(
                getAllGrandPrixResultOfYear(year).first { itt.lineUpId.driverId == it.driver.id.toInt() },
                teamService.findById(itt.lineUpId.teamId.toBigInteger()).orElse(null),
                itt.year
            )
        }
        .groupBy({ it.team }, { it.driver.grandPrixPoints })
        .map { itt ->
            TeamStandingDto(
                itt.key,
                .0,
                itt.value.flatMap { it.values }
                    .sortedBy { it.grandPrix.id }
                    .groupBy({ it.grandPrix }, { it.points })
                    .map { PointsPerGrandPrixDto(it.key, it.value.sum()) }
            )
        }
        .map {
            TeamStandingDto(
                it.team,
                it.grandPrixPoints.sumOf(PointsPerGrandPrixDto::points),
                it.grandPrixPoints
            )
        }
        .sortedByDescending { it.totalPoints }
        .toList()
}
