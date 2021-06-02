package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteAlias
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.News
import ru.project.f1.service.NewsService
import java.time.LocalDateTime

@Route("news/edit/:id?")
@RouteAlias(value = "news/add/")
@Component
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
            horizontalLayout {
                width = "100%"
                menuBar {
                    setSizeFull()
                    addItem(Icon(VaadinIcon.ARROW_CIRCLE_LEFT)) {
                        Notification.show("News")
                        UI.getCurrent().navigate("news")
                    }
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
                                    News("Mock author", LocalDateTime.now())
                                } else newsForEdit
                                news.title = newsTitle.value
                                news.text = newsText.value
                                newsService.save(news)
                                UI.getCurrent().navigate("news")
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