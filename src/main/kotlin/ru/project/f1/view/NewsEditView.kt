package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.button.Button
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
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.view.fragment.HeaderBarView.Companion.headerBar
import java.time.LocalDateTime

@Route("news/edit/:id?")
@RouteAlias(value = "news/add/")
@Component
@PageTitle("Edit news | F1")
@PreserveOnRefresh
@UIScope
@Secured("MODERATOR", "ADMIN")
class NewsEditView : KComposite(), BeforeEnterObserver {

    @Autowired
    private lateinit var newsService: NewsService
    private lateinit var addNewsButton: Button
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
                    addKeyDownListener {
                        if (!newsText.isEmpty && !isEmpty) {
                            addNewsButton.isEnabled = true
                        }
                    }
                }
                newsText = textArea {
                    setSizeFull()
                    placeholder = "News text"
                    addKeyDownListener {
                        if (!isEmpty && !newsTitle.isEmpty) {
                            addNewsButton.isEnabled = true
                        }
                    }
                }
                horizontalLayout {
                    width = "100%"
                    horizontalLayout { width = "100%" }
                    horizontalLayout {
                        addNewsButton = button("Save news") {
                            isEnabled = false
                            onLeftClick {
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
                addNewsButton.isEnabled = true
            }
        } else {
            newsTitle.clear()
            newsText.clear()
        }
    }
}