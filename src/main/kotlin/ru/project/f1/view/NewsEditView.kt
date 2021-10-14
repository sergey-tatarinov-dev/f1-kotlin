package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Setter
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Component
import ru.project.f1.entity.News
import ru.project.f1.service.NewsService
import ru.project.f1.utils.SecurityUtils.Companion.getUser
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.utils.UiUtils.Companion.show
import ru.project.f1.view.fragment.HeaderBarView.Companion.headerBar
import java.time.LocalDateTime

@Route("news/edit/:id?")
@RouteAlias(value = "news/add/")
@Component
@PageTitle("F1 | Edit news")
@PreserveOnRefresh
@UIScope
@Secured("MODERATOR", "ADMIN")
class NewsEditView : KComposite(), BeforeEnterObserver {

    @Autowired
    private lateinit var newsService: NewsService
    private lateinit var saveNewsButton: Button
    private lateinit var newsTitle: TextField
    private lateinit var newsText: TextArea
    private lateinit var newId: String
    private lateinit var newsForEdit: News

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"
                height = "100%"
                h1("Add news")
                newsTitle = textField {
                    width = "100%"
                    placeholder = "News title"
                }
                newsText = textArea {
                    setSizeFull()
                    placeholder = "News text"
                }
                horizontalLayout {
                    width = "100%"
                    horizontalLayout { width = "100%" }
                    horizontalLayout {
                        saveNewsButton = button("Save news") {
                            onLeftClick {
                                if (newsText.isEmpty || newsTitle.isEmpty){
                                    show("Title or text isn't filled")
                                } else {
                                    val news = if (newId.isEmpty()) {
                                        News(getUser(), LocalDateTime.now())
                                    } else newsForEdit
                                    news.title = newsTitle.value
                                    news.text = newsText.value
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
    }

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