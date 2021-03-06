package ru.project.f1.view

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.data.renderer.NumberRenderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.project.f1.entity.Standing
import ru.project.f1.service.GrandPrixResultService
import java.text.NumberFormat

@CssImport(themeFor = "vaadin-grid", value = "./styles/style.css")
class StandingView : HasImage() {

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

    fun <T : Standing> Grid<T>.addColumns(block: Grid<T>.() -> Unit = {}) {
        apply(block)
        val grandPrixResults = grandPrixResultService.findAll(PageRequest.of(0, 600)).toMutableList()
        grandPrixResults
            .sortedBy { it.grandPrix.date }
            .groupBy { it.grandPrix }
            .toList()
            .forEachIndexed { index, standing ->
                run {
                    addColumn(
                        NumberRenderer(
                            {
                                it.points[index]
                            }, NumberFormat.getNumberInstance()
                        )
                    ).apply {
                        setClassNameGenerator { "min-padding" }
                        width = "1%"
                        textAlign = ColumnTextAlign.CENTER

                        val image = imageById(standing.first.track.country.id, standing.first.fullName) {
                            height = "20px"
                        }
                        setHeader(
                            Anchor("/grand-prix/${standing.first.id}", image).apply {
                                setTitle(standing.first.fullName)
                                setClassNameGenerator { "min-padding" }
                            }
                        )
                    }
                }
            }
        getColumnByKey("sum").apply {
            setClassNameGenerator { "min-padding" }
            isSortable = false
            width = "2%"
            textAlign = ColumnTextAlign.CENTER
            style.set("font-size", "13px")
        }
    }
}