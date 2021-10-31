package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.service.FileService
import ru.project.f1.utils.SecurityUtils.Companion.getUser
import ru.project.f1.utils.UiUtils.Companion.avatarFromPath
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar

@Route("profile/:login")
@Component
@PageTitle("F1 | Profile")
@PreserveOnRefresh
@UIScope
class ProfileView : KComposite(), BeforeEnterObserver {

    @Autowired
    private lateinit var fileService: FileService

    private lateinit var div: Div
    private lateinit var login: String

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "17%"
                h1("My profile")
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
        val fileId = user.userPic?.id
        val file = fileService.findById(fileId!!)
        val avatarFromPath = avatarFromPath(file.orElseThrow().absolutePath, user.login).apply {
            setHeightFull()
        }
        div.apply {
            horizontalAlignSelf = FlexComponent.Alignment.CENTER
            removeAll()
            add(avatarFromPath)
        }
    }
}