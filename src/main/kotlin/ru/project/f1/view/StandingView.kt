package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.KComposite
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.data.renderer.NumberRenderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.project.f1.entity.Standing
import ru.project.f1.service.FileService
import ru.project.f1.service.GrandPrixResultService
import ru.project.f1.utils.UiUtils.Companion.imageFromPath
import java.text.NumberFormat

open class StandingView : KComposite() {

    @Autowired
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var grandPrixResultService: GrandPrixResultService

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
                        val src = findById.orElseThrow().absolutePath
                        val alt = standing.first.fullName
                        val image = imageFromPath(src, alt).apply {
                            height = "20px"
                        }
                        setHeader(
                            Anchor("/grand-prix/${standing.first.id}", image)
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
}