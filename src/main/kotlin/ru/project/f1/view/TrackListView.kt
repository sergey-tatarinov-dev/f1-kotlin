package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.*
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
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title

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
                title("Tracks")

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.NONE)
                    isAllRowsVisible = true
                    columnFor(Track::circuitName) {
                        isSortable = false
                    }
                    columnFor(Track::length) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    columnFor(Track::lapCount) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    addThemeVariants(LUMO_NO_BORDER, LUMO_NO_ROW_BORDERS, LUMO_ROW_STRIPES)
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