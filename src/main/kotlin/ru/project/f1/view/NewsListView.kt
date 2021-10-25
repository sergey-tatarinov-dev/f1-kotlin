package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.notification.Notification
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
import ru.project.f1.utils.SecurityUtils.Companion.isUserLoggedIn
import ru.project.f1.utils.UiUtils.Companion.customDialog
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
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
                    h1("News") {
                        style.set("flex-grow", "1")
                    }
                    suggestedNewsButton = button().apply {
                        isVisible = false
                        onLeftClick {
                            setLocation("/news/suggested")
                        }
                    }
                }

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.SINGLE)
                    addSelectionListener {
                        it.firstSelectedItem.ifPresentOrElse({
                            editNewsButton.isEnabled = true
                            deleteNewsButton.isEnabled = true
                        }, {
                            editNewsButton.isEnabled = false
                            deleteNewsButton.isEnabled = false
                        })
                    }
                    isHeightByRows = true
                    addColumnFor(News::title) {
                        isSortable = false
                    }
                    addColumnFor(createdDateRef, renderer) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    addItemDoubleClickListener {
                        setLocation("/news/read/${it.item.id}")
                    }
                    addThemeVariants(
                        GridVariant.LUMO_NO_BORDER,
                        GridVariant.LUMO_NO_ROW_BORDERS,
                        GridVariant.LUMO_ROW_STRIPES
                    )
                }
                if (isUserLoggedIn()) {
                    horizontalLayout {
                        setWidthFull()
                        horizontalLayout {
                            setWidthFull()
                            button(if (getUser().role == Role.USER) "Suggest news" else "Add news").apply {
                                setPrimary()
                                onLeftClick {
                                    setLocation(if (getUser().role == Role.USER) "/news/suggest/" else "/news/add/")
                                }
                            }
                            if (getUser().role in listOf(Role.ADMIN, Role.MODERATOR)) {
                                editNewsButton = button("Edit news") {
                                    isEnabled = false
                                    onLeftClick {
                                        val selectedNewsId = grid.selectedItems.toList()[0].id
                                        setLocation("/news/edit/${selectedNewsId}")
                                    }
                                }
                            }
                        }
                        if (getUser().role in listOf(Role.ADMIN, Role.MODERATOR)) {
                            horizontalLayout {
                                deleteNewsButton = button("Delete news") {
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
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        news = newsService.findAllBySuggestedAndDeleted(false, false, PageRequest.of(0, 20)).toMutableList()
        grid.setItems(news.sortedWith(comparatorForGrid))
        suggestedCount = newsService.countAllBySuggested(true)
        suggestedNewsButton.apply {
            isVisible = suggestedCount > 0 && isUserLoggedIn() && getUser().role in listOf(Role.ADMIN, Role.MODERATOR)
            text = "Suggested news (${suggestedCount})"
        }
    }

    fun deleteNews(selectedNews: News) {
        newsService.deleteById(selectedNews.id)
        news.remove(selectedNews)
        grid.setItems(news.sortedWith(comparatorForGrid))
        Notification.show("News was be successfully deleted")
    }

}