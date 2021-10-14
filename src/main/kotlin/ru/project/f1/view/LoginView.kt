package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import ru.project.f1.security.CustomRequestCache
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.utils.UiUtils.Companion.show
import ru.project.f1.view.fragment.HeaderBarView.Companion.loginHeaderBar


@Route("login")
@Component
@PWA(name = "F1 News", shortName = "F1", iconPath = "/img/icons/icon.png")
@PageTitle("F1 | Login")
@PreserveOnRefresh
@UIScope
class LoginView : KComposite() {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager
    var requestCache = CustomRequestCache()

    val root = ui {
        verticalLayout {
            horizontalLayout {
                height = "20%"
                loginHeaderBar { }
            }
            setSizeFull()
            loginForm {
                alignSelf = FlexComponent.Alignment.CENTER
                action = "login"
                addLoginListener {
                    val authentication = authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken(
                            it.username,
                            it.password
                        )
                    )
                    if (authentication != null) {
                        SecurityContextHolder.getContext().authentication = authentication
                        val url = requestCache.resolveRedirectUrl()
                        println(url)
                        setLocation(url)
                        show("goto $url")
                    }

                    Notification.show("Logged in")
                }
            }
            formLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "15%"
                button("Register") {
                    onLeftClick { setLocation("/register") }
                }
            }
        }
    }
}