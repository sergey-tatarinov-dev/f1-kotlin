package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.GrandPrixResultPerDriver
import ru.project.f1.service.FileService
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.utils.UiUtils.Companion.imageFromPath
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar

@Route("grand-prix/:id")
@Component
@PreserveOnRefresh
@UIScope
class GrandPrixView : StandingView(), BeforeEnterObserver {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

    @Autowired
    private lateinit var fileService: FileService
    private lateinit var grandPrixId: String
    private lateinit var titleLayout: HorizontalLayout
    private lateinit var grid: Grid<GrandPrixResultPerDriver>

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                titleLayout = horizontalLayout {}
                grid = grid {
                    setSelectionMode(Grid.SelectionMode.NONE)
                    isAllRowsVisible = true
                    addColumnFor(GrandPrixResultPerDriver::driverName) {
                        isSortable = false
                    }
                    addColumnFor(GrandPrixResultPerDriver::teamName) {
                        isSortable = false
                    }
                    addColumnFor(GrandPrixResultPerDriver::position) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        val grandPrixList = grandPrixResultService.findAllByGrandPrixId(grandPrixId.toInt())
        grid.setItems(grandPrixList)
        val grandPrix = grandPrixResultService.findGrandPrixById(grandPrixId.toBigInteger()).orElseThrow()
        val pageTitle = "${grandPrix.fullName} ${grandPrix.date.year}"
        UI.getCurrent().page.setTitle(pageTitle)
        val country = grandPrix.track.country
        val file = fileService.findById(country.f1File.id).orElseThrow()
        titleLayout.add(
            imageFromPath(file.absolutePath, country.name).apply {
                height = "30px"
                width = "45px"
            },
            H1(pageTitle)
        )
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        grandPrixId = event!!.routeParameters["id"].get()
    }
}