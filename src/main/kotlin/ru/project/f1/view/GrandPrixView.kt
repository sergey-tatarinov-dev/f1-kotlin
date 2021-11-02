package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
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
import ru.project.f1.entity.GrandPrixResultPerGrandPrix
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar

@Route("grand-prix/:id")
@Component
@PreserveOnRefresh
@UIScope
class GrandPrixView : StandingView(), BeforeEnterObserver, HasDynamicTitle {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService
    private lateinit var grandPrixId: String
    private lateinit var titleLayout: HorizontalLayout
    private lateinit var grid: Grid<GrandPrixResultPerGrandPrix>
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
            addColumnFor(GrandPrixResultPerGrandPrix::position) {
                isSortable = false
                isExpand = false
            }

            addColumn(ComponentRenderer(::Anchor) { anchor: Anchor, grandPrixResultPerGrandPrix: GrandPrixResultPerGrandPrix ->
                val split = grandPrixResultPerGrandPrix.driverName.split(" ")
                anchor.apply {
                    text = "${split[0]} ${split[1].toUpperCase()}"
                    href = "/driver/${grandPrixResultPerGrandPrix.driverId}"
                }
            }).setHeader("Driver")

            addComponentColumn { grandPrixResultPerGrandPrix ->
                imageById(grandPrixResultPerGrandPrix.countryId, grandPrixResultPerGrandPrix.driverName) {
                    height = "20px"
                    width = "30px"
                }
            }.apply {
                isExpand = false
            }

            addColumnFor(GrandPrixResultPerGrandPrix::teamName) {
                isSortable = false
            }.setHeader("Team")

            addComponentColumn { grandPrixResultPerGrandPrix ->
                imageById(grandPrixResultPerGrandPrix.logoId, grandPrixResultPerGrandPrix.teamName) {
                    height = "25px"
                    width = "25px"
                }
            }.apply {
                isExpand = false
            }
            setItems(grandPrixList)
        }
        val grandPrix = grandPrixResultService.findGrandPrixById(grandPrixId.toBigInteger()).orElseThrow()
        pageTitle = "${grandPrix.fullName} ${grandPrix.date.year}"
        val country = grandPrix.track.country
        titleLayout.apply {
            removeAll()
            add(
                imageById(country.f1File.id, country.name) {
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