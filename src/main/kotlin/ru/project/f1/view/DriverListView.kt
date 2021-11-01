package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.data.renderer.TextRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.Driver
import ru.project.f1.service.DriverService
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar

@Route("drivers")
@Component
@PageTitle("F1 | Drivers")
@PreserveOnRefresh
@UIScope
class DriverListView : KComposite() {

    @Autowired
    private lateinit var driverService: DriverService
    private lateinit var grid: Grid<Driver>
    private lateinit var drivers: MutableList<Driver>

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                h1("Drivers")

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.NONE)
                    isAllRowsVisible = true
                    addColumnFor(Driver::name, TextRenderer { "${it.name} ${it.surname}" }) {
                        isSortable = false
                    }
                    addColumnFor(Driver::raceNumber) {
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
        drivers = driverService.findAll(PageRequest.of(0, 20)).toMutableList()
        grid.setItems(drivers)
    }
}