package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSelectionModel
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.Post
import ru.project.f1.service.PostService
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Route("news")
@Component
class NewsView : KComposite() {

    @Autowired
    private lateinit var postService: PostService
    private lateinit var grid: Grid<Post>
    private val createdDateRef = Post::createdDate
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    private var renderer = LocalDateTimeRenderer(createdDateRef, formatter)
    private lateinit var editPostButton: Button
    private lateinit var deletePostButton: Button

    val root = ui {
        verticalLayout {
            horizontalLayout {
                width = "100%"
                menuBar {
                    setSizeFull()
                    addItem("News") {
                        Notification.show("News")
                        UI.getCurrent().navigate("news")
                    }
                    addItem("Championship Standings") {
                        Notification.show("Championship Standings")
                        UI.getCurrent().navigate("championship-standings")
                    }
                    addItem("Driver Standings") {
                        Notification.show("Driver Standings")
                        UI.getCurrent().navigate("driver-standings")
                    }
                }
                menuBar {
                    addItem("Login") {
                        Notification.show("Login")
                        UI.getCurrent().navigate("welcome-login")
                    }
                }
            }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                h1("News")

                grid = grid {
                    setSelectionMode(Grid.SelectionMode.SINGLE)
                    addSelectionListener {
                        it.firstSelectedItem.ifPresentOrElse({
                            editPostButton.isEnabled = true
                            deletePostButton.isEnabled = true
                        }, {
                            editPostButton.isEnabled = false
                            deletePostButton.isEnabled = false
                        })
                    }
                    flexGrow = 1.0
                    addColumnFor(Post::title) {
                        isSortable = false
                    }
                    addColumnFor(createdDateRef, renderer) {
                        isSortable = false
                        textAlign = ColumnTextAlign.END
                    }
                    addItemDoubleClickListener {
                        UI.getCurrent().navigate("news/read/${it.item.id}")
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
                        button("Add post") {
                            setPrimary()
                            onLeftClick {
                                UI.getCurrent().navigate("news/add/")
                            }
                        }
                        editPostButton = button("Edit post") {
                            isEnabled = false
                            onLeftClick {
                                val selectedPostId = grid.selectedItems.toList()[0].id
                                UI.getCurrent().navigate("news/edit/${selectedPostId}")
                            }
                        }
                    }
                    horizontalLayout {
                        deletePostButton = button("Delete post") {
                            isEnabled = false
                            onLeftClick {
                                postService.deleteById(grid.selectedItems.toList()[0].id)
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
        val posts = postService.findAll(PageRequest.of(0, 20))
        grid.setItems(posts.toList().sortedWith(compareByDescending { it.createdDate }))
    }

}