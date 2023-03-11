package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.GrandPrix
import ru.project.f1.entity.GrandPrixResultPerGrandPrix
import ru.project.f1.service.CountryService
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.service.GrandPrixService
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar

@Route("grand-prix/:id")
@Component
@PreserveOnRefresh
@UIScope
class GrandPrixView : HasImage(), BeforeEnterObserver, HasDynamicTitle {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService
    @Autowired
    private lateinit var countryService: CountryService
    @Autowired
    private lateinit var grandPrixService: GrandPrixService
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
        val grandPrixResultPerGrandPrixList = grandPrixResultService.findAllByGrandPrixId(grandPrixId.toInt())
        val grandPrix = grandPrixService.findGrandPrixById(grandPrixId.toBigInteger()).orElseThrow()
        grid.apply {
            removeAllColumns()
            addMyColumns(grandPrix)
            addThemeVariants(LUMO_NO_BORDER, LUMO_NO_ROW_BORDERS, LUMO_ROW_STRIPES)
            setItems(grandPrixResultPerGrandPrixList)
        }
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

    fun Grid<GrandPrixResultPerGrandPrix>.addMyColumns(grandPrix: GrandPrix) {
        columnFor(GrandPrixResultPerGrandPrix::position) {
            isSortable = false
            isExpand = false
        }

        addComponentColumn {
            val country = countryService.findById(it.countryId).orElseThrow()
            val image = imageById(country.f1File.id, it.driverName) {
                height = "20px"
                width = "30px"
            }
            val split = it.driverName.split(" ")
            val text = Span("${split[0]} ${split[1].uppercase()}").apply {
                style.apply {
                    set("margin-left", "10px")
                    set("align-self", "center")
                }
            }
            Anchor("/driver/${it.driverId}", image, text).apply {
                style.set("display", "inline-flex")
            }
        }.setHeader("Driver")
        addComponentColumn {
            val team = grandPrixResultService.findTeamByDriverIdAndYear(it.driverId, grandPrix.date.year)
            team.country
            val image = imageById(team.file.id, team.name) {
                height = "25px"
                width = "25px"
            }
            val text = Span(team.name).apply {
                style.apply {
                    set("margin-left", "10px")
                    set("align-self", "center")
                }
            }
            Div(image, text)
        }.apply {
            setHeader("Team")
            isSortable = false
        }
    }
}