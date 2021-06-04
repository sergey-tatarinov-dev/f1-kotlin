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
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.News
import ru.project.f1.service.NewsService
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.view.fragment.HeaderBarView.Companion.headerBar
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Route("news")
@Component
@PageTitle("News | F1")
@PreserveOnRefresh
@UIScope
class NewsView : KComposite() {

    @Autowired
    private lateinit var newsService: NewsService
    private lateinit var grid: Grid<News>
    private val createdDateRef = News::createdDate
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    private var renderer = LocalDateTimeRenderer(createdDateRef, formatter)
    private lateinit var editNewsButton: Button
    private lateinit var deleteNewsButton: Button

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                h1("News")

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
                    flexGrow = 1.0
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
                horizontalLayout {
                    width = "100%"
                    horizontalLayout {
                        width = "100%"
                        button("Add news") {
                            setPrimary()
                            onLeftClick {
                                setLocation("/news/add/")
                            }
                        }
                        editNewsButton = button("Edit news") {
                            isEnabled = false
                            onLeftClick {
                                val selectedNewsId = grid.selectedItems.toList()[0].id
                                setLocation("/news/edit/${selectedNewsId}")
                            }
                        }
                    }
                    horizontalLayout {
                        deleteNewsButton = button("Delete news") {
                            isEnabled = false
                            onLeftClick {
                                newsService.deleteById(grid.selectedItems.toList()[0].id)
                                Notification.show("News was be successfully deleted")
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        val news = newsService.findAll(PageRequest.of(0, 20))
        grid.setItems(news.toList().sortedWith(compareByDescending { it.createdDate }))
    }

}