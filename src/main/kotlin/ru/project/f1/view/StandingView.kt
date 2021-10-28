package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.KComposite
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.project.f1.entity.Standing
import ru.project.f1.service.FileService
import ru.project.f1.service.GrandPrixResultService
import java.io.FileInputStream
import java.text.NumberFormat

open class StandingView : KComposite() {

    @Autowired
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

    private fun imageFromPath(src: String, alt: String) =
        Image(
            StreamResource(alt,
                InputStreamFactory {
                    FileInputStream(src)
                }), alt
        ).apply {
            setWidthFull()
            height = "20px"
        }

    fun <T : Standing> Grid<T>.addColumns(block: Grid<T>.() -> Unit = {}) {
        apply(block)
        val grandPrixResults = grandPrixResultService.findAll(PageRequest.of(0, 400)).toMutableList()
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
                        width = "1%"
                        textAlign = ColumnTextAlign.CENTER

                        val findById = fileService.findById(standing.first.track.country.id)
                        val src = findById.orElse(null).absolutePath
                        val alt = standing.first.fullName
                        setHeader(
                            Anchor("/grand-prix/${standing.first.id}", imageFromPath(src, alt))
                                .apply { setTitle(standing.first.fullName) }
                        )
                    }
                }
            }
        getColumnByKey("sum").apply {
            isSortable = false
            width = "2%"
            textAlign = ColumnTextAlign.CENTER
            grid.style.set("font-size", "13px")
        }
    }

    /*fun <T : Standing> addColumns(grid: Grid<T>) {
        val grandPrixResults = grandPrixResultService.findAll(PageRequest.of(0, 400)).toMutableList()
        grandPrixResults
            .sortedBy { it.grandPrix.date }
            .groupBy { it.grandPrix }
            .toList()
            .forEachIndexed { index, standing ->
                run {
                    grid.addColumn(
                        NumberRenderer(
                            {
                                it.points[index]
                            }, NumberFormat.getNumberInstance()
                        )
                    ).apply {
                        width = "1%"
                        textAlign = ColumnTextAlign.CENTER

                        val findById = fileService.findById(standing.first.track.country.id)
                        val src = findById.orElse(null).absolutePath
                        val alt = standing.first.fullName
                        setHeader(
                            Anchor("/grand-prix/${standing.first.id}", imageFromPath(src, alt))
                                .apply { setTitle(standing.first.fullName) }
                        )
                    }
                }
            }
        grid.getColumnByKey("sum").apply {
            isSortable = false
            width = "2%"
            textAlign = ColumnTextAlign.CENTER
            grid.style.set("font-size", "13px")
        }
    }*/
}