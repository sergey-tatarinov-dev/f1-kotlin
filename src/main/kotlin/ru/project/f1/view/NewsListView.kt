package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.refresh
import com.github.mvysny.kaributools.setPrimary
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
import com.vaadin.flow.router.RouteAlias
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.News
import ru.project.f1.entity.Role
import ru.project.f1.service.NewsService
import ru.project.f1.utils.SecurityUtils.Companion.getUser
import ru.project.f1.utils.SecurityUtils.Companion.isAdminOrModerator
import ru.project.f1.utils.SecurityUtils.Companion.isUserLoggedIn
import ru.project.f1.utils.UiUtils.Companion.customDialog
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.utils.UiUtils.Companion.successBox
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Route("")
@RouteAlias(value = "news")
@Component
@PageTitle("F1 | News")
@PreserveOnRefresh
@UIScope
class NewsListView : KComposite() {

    @Autowired
    private lateinit var newsService: NewsService
    private lateinit var grid: Grid<News>
    private lateinit var editNewsButton: Button
    private lateinit var deleteNewsButton: Button
    private lateinit var suggestedNewsButton: Button
    private lateinit var news: MutableList<News>
    private val createdDateRef = News::createdDate
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    private var renderer = LocalDateTimeRenderer(createdDateRef, formatter)
    private val comparatorForGrid: Comparator<News> = compareByDescending { it.createdDate }
    private var suggestedCount: Int = 0

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                horizontalLayout {
                    setWidthFull()
                    title("News")
                    suggestedNewsButton = button().apply {
                        isVisible = false
                        onLeftClick {
                            setLocation("/news/suggested")
                        }
                    }
                }

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.SINGLE)
                    isAllRowsVisible = true
                    columnFor(News::title) {
                        isSortable = false
                    }
                    columnFor(createdDateRef, renderer) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    addItemDoubleClickListener {
                        setLocation("/news/read/${it.item.id}")
                    }
                    addThemeVariants(LUMO_NO_BORDER, LUMO_NO_ROW_BORDERS, LUMO_ROW_STRIPES)
                }
                if (isUserLoggedIn()) {
                    horizontalLayout {
                        setWidthFull()
                        button("${if (getUser().role == Role.USER) "Suggest" else "Add"} news").apply {
                            setPrimary()
                            onLeftClick {
                                setLocation("/news/${if (getUser().role == Role.USER) "suggest" else "add"}/")
                            }
                        }
                        if (isAdminOrModerator()) {
                            editNewsButton = button("Edit news") {
                                isEnabled = false
                                onLeftClick {
                                    val selectedNewsId = grid.selectedItems.toList()[0].id
                                    setLocation("/news/edit/${selectedNewsId}")
                                }
                            }
                        }
                        if (isAdminOrModerator()) {
                            deleteNewsButton = button("Delete news") {
                                style.set("margin-left", "auto")
                                isEnabled = false
                                onLeftClick {
                                    customDialog("Are you sure you want to delete the news?") {
                                        deleteNews(grid.selectedItems.toList()[0])
                                        grid.refresh()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        news = newsService.findAllBySuggestedAndDeleted(false, false, PageRequest.of(0, 20)).toMutableList()
        grid.apply {
            setItems(news.sortedWith(comparatorForGrid))
            addSelectionListener {
                if (isUserLoggedIn()) {
                    editNewsButton.isEnabled = it.firstSelectedItem.isPresent
                    deleteNewsButton.isEnabled = it.firstSelectedItem.isPresent
                }
            }
        }
        suggestedCount = newsService.countAllBySuggested(true)
        suggestedNewsButton.apply {
            isVisible = suggestedCount > 0 && isUserLoggedIn() && isAdminOrModerator()
            text = "Suggested news (${suggestedCount})"
        }
    }

    fun deleteNews(selectedNews: News) {
        newsService.deleteById(selectedNews.id)
        news.remove(selectedNews)
        grid.setItems(news.sortedWith(comparatorForGrid))
        successBox("News has been successfully deleted")
    }

}