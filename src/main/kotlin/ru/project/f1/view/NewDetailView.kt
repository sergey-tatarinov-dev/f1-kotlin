package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.service.NewsService
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Route("news/read/:id")
@Component
class NewDetailView : KComposite(), BeforeEnterObserver {

    @Autowired
    private lateinit var newsService: NewsService
    private lateinit var newId: String
    private lateinit var title: H1
    private lateinit var timeSpan: Span
    private lateinit var div: Div
    private var formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)!!

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

                horizontalLayout {
                    width = "100%"
                    title = h1 { width = "100%" }
                    timeSpan = span { width = "15%" }
                }
                //text = p { html }
                div = div {}
            }
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        newId = event!!.routeParameters["id"].get()
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        if (newId.isNotEmpty()) {
            val news = newsService.findById(newId.toBigInteger())
            news.ifPresent {
                title.text = it.title
                div.removeAll()
                div.add(Html("<div><p>${it.text.replace("\n", "</p><p>")}</p></div>"))
                timeSpan.text = it.createdDate.format(formatter)
            }
        }
    }

}