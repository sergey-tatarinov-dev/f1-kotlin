package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.Team
import ru.project.f1.service.TeamService
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar

@Route("teams")
@Component
@PageTitle("F1 | Teams")
@PreserveOnRefresh
@UIScope
class TeamListView : KComposite() {

    @Autowired
    private lateinit var teamService: TeamService
    private lateinit var grid: Grid<Team>
    private lateinit var teams: MutableList<Team>

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                h1("Teams")

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.NONE)
                    isAllRowsVisible = true
                    addColumnFor(Team::fullName) {
                        isSortable = false
                    }
                    addColumnFor(Team::name) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    addThemeVariants(LUMO_NO_BORDER, LUMO_NO_ROW_BORDERS, LUMO_ROW_STRIPES)
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        teams = teamService.findAll(PageRequest.of(0, 20)).toMutableList()
        grid.setItems(teams)
    }
}