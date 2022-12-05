package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.stereotype.Component
import ru.project.f1.utils.SecurityUtils.Companion.getUser
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.title

@Route("profile/:login")
@Component
@PageTitle("F1 | Profile")
@PreserveOnRefresh
@UIScope
class ProfileView : HasImage(), BeforeEnterObserver {

    private lateinit var div: Div
    private lateinit var login: String

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "17%"
                title("My profile")
                div = div {}
                textField("Login") {
                    value = getUser().login
                    setWidthFull()
                }
                button("Change password") {
                    setWidthFull()
                    onLeftClick { setLocation("/change-password") }
                }
            }
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        login = event!!.routeParameters["login"].orElse("")
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        val user = getUser()
        div.apply {
            horizontalAlignSelf = FlexComponent.Alignment.CENTER
            removeAll()
            add(
                avatarById(user.userPic?.id!!, user.login) {
                    setHeightFull()
                }
            )
        }
    }
}