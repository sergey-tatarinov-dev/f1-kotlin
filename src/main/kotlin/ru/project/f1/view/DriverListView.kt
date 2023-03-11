package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.Action
import ru.project.f1.entity.Driver
import ru.project.f1.service.DriverService
import ru.project.f1.utils.SecurityUtils
import ru.project.f1.utils.UiUtils
import ru.project.f1.view.dialog.DriverDialog
import ru.project.f1.view.fragment.Components
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title

@Route("drivers")
@Component
@PageTitle("F1 | Drivers")
@PreserveOnRefresh
@UIScope
class DriverListView : HasImage() {

    @Autowired
    private lateinit var driverService: DriverService

    @Autowired
    private lateinit var driverDialog: DriverDialog
    private lateinit var grid: Grid<Driver>
    private lateinit var drivers: MutableList<Driver>
    private lateinit var select: Select<String>

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                width = "65%"
                alignSelf = FlexComponent.Alignment.CENTER
                horizontalLayout {
                    style.set("margin-top", "0px")
                    setWidthFull()
                    title("Drivers")
                    if (SecurityUtils.isUserLoggedIn() && SecurityUtils.isAdminOrModerator()) {
                        button("Add a Driver") {
                            onLeftClick {
                                driverDialog.openAndThen(Action.CREATE) {
                                    updateView()
                                }
                            }
                        }
                    }
                    select = select {
                        label = "Year"
                    }
                }

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.NONE)
                    isAllRowsVisible = true
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        val year = driverService.findAllDriverYears().map { it.toString() }.last()
        drivers = driverService.findAllDriversByYear(year.toInt()).toMutableList()
        grid.apply {
            removeAllColumns()

            addComponentColumn {
                val driver =
                    driverService.findById(it.id).orElseThrow()
                val country = driver.country
                val image = imageById(country.f1File.id, it.name) {
                    height = "20px"
                    width = "30px"
                }
                val text = Span("${it.name} ${it.surname.uppercase()}").apply {
                    style.apply {
                        set("margin-left", "10px")
                        set("align-self", "center")
                    }
                }
                Anchor("/driver/${it.id}", image, text).apply {
                    style.set("display", "inline-flex")
                }
            }.setHeader("Name")
            columnFor(Driver::raceNumber) {
                isSortable = false
                textAlign = ColumnTextAlign.END
            }

            if (SecurityUtils.isUserLoggedIn() && SecurityUtils.isAdminOrModerator()) {
                addComponentColumn {
                    Components.createEditButton {
                        driverDialog.openAndThen(Action.EDIT, it) {
                            updateView()
                        }
                    }
                }.apply {
                    flexGrow = 0
                    width = "70px"
                    textAlign = ColumnTextAlign.CENTER
                }
                addComponentColumn {
                    Components.createRemoveButton {
                        driverService.deleteById(it.id)
                        UiUtils.successBox("Driver has been successfully deleted")
                        updateView()
                    }
                }.apply {
                    flexGrow = 0
                    width = "70px"
                    textAlign = ColumnTextAlign.CENTER
                }
            }
            setItems(drivers)
            addThemeVariants(LUMO_NO_BORDER, LUMO_NO_ROW_BORDERS, LUMO_ROW_STRIPES)
        }

        select.apply {
            val years = driverService.findAllDriverYears().map { it.toString() }.toMutableList()
            setItems(years)
            value = years.last()
            addValueChangeListener {
                if (it != null && it.value != null) {
                    grid.setItems(driverService.findAllDriversByYear(it.value.toInt()).toMutableList())
                }
            }
        }
    }

    fun updateView() {
        val years = driverService.findAllDriverYears().map { it.toString() }.toMutableList()
        select.setItems(years)
        select.value = years.last()
        grid.setItems(driverService.findAllDriversByYear(years.last().toInt()).toMutableList())
    }
}