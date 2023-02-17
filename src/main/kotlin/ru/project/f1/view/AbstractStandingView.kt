package ru.project.f1.view

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.TextRenderer
import org.springframework.beans.factory.annotation.Autowired
import ru.project.f1.dto.*
import ru.project.f1.entity.GrandPrixResult
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.service.GrandPrixService

@CssImport(themeFor = "vaadin-grid", value = "./styles/style.css")
abstract class AbstractStandingView<T> : HasImage() where T : StandingDto {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

    @Autowired
    private lateinit var grandPrixService: GrandPrixService
    abstract var grid: Grid<T>
    abstract val renderer: ComponentRenderer<Anchor, T>
    protected lateinit var select: Select<String>

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)

        val years = grandPrixService.findAllYears().map { it.toString() }
        updateGrid(years.last().toInt())

        select.apply {
            setItems(years)
            value = years.last()
            addValueChangeListener {
                if (it != null && it.value != null) {
                    updateGrid(it.value.toInt())
                }
            }
        }
    }

    fun updateGrid(year: Int) {
        val items = getItems(year)
        grid.apply {
            removeAllColumns()
            setItems(items)
            addColumn(renderer).setHeader("Name")
            addColumn(TextRenderer { doubleToString(it.totalPoints) })
                .setClassNameGenerator { "bold" }
                .setHeader("Sum")
                .apply {
                    isSortable = false
                    width = "2%"
                    textAlign = ColumnTextAlign.CENTER
                    style.set("font-size", "13px")
                }
            addPointsColumns(year)
        }
    }

    fun addPointsValueToGrid(id: Int, gpp: PointsPerGrandPrixDto) {
        grid.apply {
            addColumn(TextRenderer {
                getPointsFromStanding(it, id)
            }).apply {
                setClassNameGenerator { "min-padding" }
                width = "1%"
                textAlign = ColumnTextAlign.CENTER

                val image = imageById(gpp.grandPrix.track.country.id, gpp.grandPrix.fullName) {
                    height = "20px"
                }
                setHeader(
                    Anchor("/grand-prix/${gpp.grandPrix.id}", image).apply {
                        setTitle(gpp.grandPrix.fullName)
                        setClassNameGenerator { "min-padding" }
                    }
                )
            }
        }
    }

    abstract fun addPointsColumns(year: Int)

    abstract fun getItems(year: Int): List<T>

    fun getAllGrandPrixResultOfYear(year: Int) = grandPrixResultService.findAllGrandPrixResultByYear(year)
        .asSequence()
        .sortedBy { it.grandPrix.id }
        .groupBy { it.driver }
        .toList()
        .map { it ->
            DriverStandingDto(
                it.first,
                it.second.sumOf(GrandPrixResult::points),
                it.second.associate { it.grandPrix.id.toInt() to PointsPerGrandPrixDto(it.grandPrix, it.points) }
            )
        }
        .sortedByDescending { it.totalPoints }
        .toList()

    fun getPointsFromStanding(it: T, id: Int): String = when (it) {
        is DriverStandingDto -> if (it.grandPrixPoints[id] != null) {
            doubleToString(it.grandPrixPoints[id]!!.points)
        } else {
            ""
        }
        is TeamStandingDto -> doubleToString(it.grandPrixPoints[id].points)
        else -> {""}
    }

    fun doubleToString(points: Double): String = if (points % 1.0 > .0) {
        points.toString()
    } else {
        points.toInt().toString()
    }
}