package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.stereotype.Component
import ru.project.f1.utils.SecurityUtils.Companion.getUser
import ru.project.f1.view.fragment.HeaderBarView.Companion.headerBar

@Route("profile/:login")
@Component
@PageTitle("Profile | F1")
@PreserveOnRefresh
@UIScope
class ProfileView : KComposite(), BeforeEnterObserver {

    private lateinit var login: String

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "17%"
                h1("My profile")
                val user = getUser()
                textField("Login") {
                    value = user.login
                    width = "100%"
                }
                button("Change password") {
                    width = "100%"
                }
            }
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        login = event!!.routeParameters["login"].orElse("")
    }
}