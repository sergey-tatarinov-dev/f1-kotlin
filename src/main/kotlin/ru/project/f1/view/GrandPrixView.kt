package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.DriverStanding
import ru.project.f1.entity.GrandPrixResultPerDriver
import ru.project.f1.service.FileService
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.utils.UiUtils.Companion.imageFromPath
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar

@Route("grand-prix/:id")
@Component
@PreserveOnRefresh
@UIScope
class GrandPrixView : StandingView(), BeforeEnterObserver, HasDynamicTitle {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

    @Autowired
    private lateinit var fileService: FileService
    private lateinit var grandPrixId: String
    private lateinit var titleLayout: HorizontalLayout
    private lateinit var grid: Grid<GrandPrixResultPerDriver>
    private lateinit var pageTitle: String

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
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        val grandPrixList = grandPrixResultService.findAllByGrandPrixId(grandPrixId.toInt())
        grid.apply {
            removeAllColumns()
            addColumn(ComponentRenderer(::Anchor) { anchor: Anchor, grandPrixResultPerDriver: GrandPrixResultPerDriver ->
                anchor.apply {
                    text = grandPrixResultPerDriver.driverName
                    href = "/driver/${grandPrixResultPerDriver.driverId}"
                }
            }).setHeader("Driver name")
            addColumnFor(GrandPrixResultPerDriver::teamName) {
                isSortable = false
            }
            addColumnFor(GrandPrixResultPerDriver::position) {
                isSortable = false
                textAlign = ColumnTextAlign.END
            }
            setItems(grandPrixList)
        }
        val grandPrix = grandPrixResultService.findGrandPrixById(grandPrixId.toBigInteger()).orElseThrow()
        pageTitle = "${grandPrix.fullName} ${grandPrix.date.year}"
        val country = grandPrix.track.country
        val file = fileService.findById(country.f1File.id).orElseThrow()
        titleLayout.apply {
            removeAll()
            add(
                imageFromPath(file.absolutePath, country.name).apply {
                    height = "30px"
                    width = "45px"
                },
                H1(pageTitle)
            )
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        grandPrixId = event!!.routeParameters["id"].get()
    }

    override fun getPageTitle(): String = pageTitle
}