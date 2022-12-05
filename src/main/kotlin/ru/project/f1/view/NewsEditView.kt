package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component
import ru.project.f1.entity.News
import ru.project.f1.service.NewsService
import ru.project.f1.utils.SecurityUtils.Companion.getUser
import ru.project.f1.utils.UiUtils.Companion.failBox
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import java.time.LocalDateTime

@Route("news/edit/:id?")
@RouteAlias.Container(RouteAlias(value = "news/suggest/"), RouteAlias(value = "news/add/"))
@Component
@PageTitle("F1 | Edit news")
@PreserveOnRefresh
@UIScope
@Secured("USER", "MODERATOR", "ADMIN")
class NewsEditView : KComposite(), BeforeEnterObserver {

    @Autowired
    private lateinit var newsService: NewsService
    private lateinit var saveNewsButton: Button
    private lateinit var newsTitle: TextField
    private lateinit var newsText: TextArea
    private lateinit var newId: String
    private lateinit var newsForEdit: News
    private lateinit var title: H1
    private var action: String = ""

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                setWidthFull()
                title = h1("")
                newsTitle = textField {
                    setWidthFull()
                    placeholder = "News title"
                }
                newsText = textArea {
                    setSizeFull()
                    placeholder = "News text"
                }
                horizontalLayout {
                    setWidthFull()
                    horizontalLayout { setWidthFull() }
                    horizontalLayout {
                        saveNewsButton = button("Save news") {
                            onLeftClick {
                                if (newsText.isEmpty || newsTitle.isEmpty) {
                                    failBox("Title or text isn't filled")
                                } else {
                                    val news = if (newId.isEmpty()) {
                                        News(getUser(), LocalDateTime.now())
                                    } else newsForEdit
                                    news.apply {
                                        title = newsTitle.value
                                        text = newsText.value
                                        suggested = action == "suggest"
                                    }
                                    newsService.save(news)
                                    setLocation("/news")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        newId = event!!.routeParameters["id"].orElse("")
        action = event.location.segments[1]
        UI.getCurrent().page.setTitle("F1 | ${getTitle(action)} news")
        title.text = "${getTitle(action)} news"
        if (action == "suggest") {
            saveNewsButton.text = "Suggest news"
        }
    }

    private fun getTitle(title: String) = if (title == "add") "Add" else if (title == "edit") "Edit" else "Suggest"

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        if (newId.isNotBlank()) {
            newsService.findById(newId.toBigInteger()).ifPresent {
                newsForEdit = it
                newsTitle.value = it.title
                newsText.value = it.text
            }
        } else {
            newsTitle.clear()
            newsText.clear()
        }
    }
}