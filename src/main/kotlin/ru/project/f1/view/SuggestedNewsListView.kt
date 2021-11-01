package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.*
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component
import ru.project.f1.entity.News
import ru.project.f1.service.NewsService
import ru.project.f1.utils.UiUtils
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Route("news/suggested")
@Component
@PageTitle("F1 | Suggested news")
@PreserveOnRefresh
@UIScope
@Secured("MODERATOR", "ADMIN")
class SuggestedNewsListView : KComposite() {

    @Autowired
    private lateinit var newsService: NewsService
    private lateinit var grid: Grid<News>
    private lateinit var publishNewsButton: Button
    private lateinit var news: MutableList<News>
    private val createdDateRef = News::createdDate
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    private var renderer = LocalDateTimeRenderer(createdDateRef, formatter)
    private val comparatorForGrid: Comparator<News> = compareByDescending { it.createdDate }

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                h1("Suggested news")

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.SINGLE)
                    addSelectionListener {
                        it.firstSelectedItem.ifPresentOrElse({
                            publishNewsButton.isEnabled = true
                        }, {
                            publishNewsButton.isEnabled = false
                        })
                    }
                    flexGrow = 1.0
                    addColumnFor(News::title) {
                        isSortable = false
                    }
                    addColumnFor(createdDateRef, renderer) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    addItemDoubleClickListener {
                        UiUtils.setLocation("/news/read/${it.item.id}")
                    }
                    addThemeVariants(LUMO_NO_BORDER, LUMO_NO_ROW_BORDERS, LUMO_ROW_STRIPES)
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        news = newsService.findAllBySuggestedAndDeleted(true, false, PageRequest.of(0, 20)).toMutableList()
        grid.setItems(news.sortedWith(comparatorForGrid))
    }
}