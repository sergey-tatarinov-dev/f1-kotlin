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
import org.springframework.stereotype.Component
import ru.project.f1.dto.DriverStandingDto
import ru.project.f1.entity.*
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title

@Route("drivers-standings")
@Component
@PageTitle("F1 | Drivers standings")
@PreserveOnRefresh
@UIScope
class DriverStandingsView : AbstractStandingView<DriverStandingDto>() {

    override lateinit var grid: Grid<DriverStandingDto>
    override val renderer: ComponentRenderer<Anchor, DriverStandingDto>
        get() = ComponentRenderer(::Anchor) { anchor: Anchor, gpr: DriverStandingDto ->
        anchor.apply {
            text = "${gpr.driver.name} ${gpr.driver.surname.uppercase()}"
            href = "/driver/${gpr.driver.id}"
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

    override fun addPointsColumns(year: Int) {
        getAllGrandPrixResultOfYear(year)[0].grandPrixPoints.forEach { (id, pointsPerGrandPrixDto) ->
            addPointsValueToGrid(id, pointsPerGrandPrixDto)
        }
    }

    override fun getItems(year: Int): List<DriverStandingDto> = getAllGrandPrixResultOfYear(year)

}