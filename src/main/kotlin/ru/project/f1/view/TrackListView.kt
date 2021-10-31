package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.Track
import ru.project.f1.service.TrackService
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar

@Route("tracks")
@Component
@PageTitle("F1 | Tracks")
@PreserveOnRefresh
@UIScope
class TrackListView : KComposite() {

    @Autowired
    private lateinit var trackService: TrackService
    private lateinit var grid: Grid<Track>
    private lateinit var tracks: MutableList<Track>

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                h1("Tracks")

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.NONE)
                    isAllRowsVisible = true
                    addColumnFor(Track::circuitName) {
                        isSortable = false
                    }
                    addColumnFor(Track::length) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    addColumnFor(Track::lapCount) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    addThemeVariants(
                        GridVariant.LUMO_NO_BORDER,
                        GridVariant.LUMO_NO_ROW_BORDERS,
                        GridVariant.LUMO_ROW_STRIPES
                    )
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        tracks = trackService.findAll(PageRequest.of(0, 20)).toMutableList()
        grid.setItems(tracks)
    }
}