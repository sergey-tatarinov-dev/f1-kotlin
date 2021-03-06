package ru.project.f1.listener

import com.vaadin.flow.component.UI
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import org.springframework.stereotype.Component
import ru.project.f1.utils.SecurityUtils
import ru.project.f1.view.LoginView
import ru.project.f1.view.NewsListView

@Component
class ConfigureUIServiceInitListener : VaadinServiceInitListener {

    override fun serviceInit(event: ServiceInitEvent) {
        event.source.addUIInitListener {
            val ui: UI = it.ui
            ui.addBeforeEnterListener(::beforeEnter)
        }
    }

    fun beforeEnter(event: BeforeEnterEvent) {
        if (!SecurityUtils.isAccessGranted(event.navigationTarget)) {
            if (SecurityUtils.isUserLoggedIn()) {
                event.rerouteTo(NewsListView::class.java)
            } else {
                event.rerouteTo(LoginView::class.java)
            }
        }
    }

}