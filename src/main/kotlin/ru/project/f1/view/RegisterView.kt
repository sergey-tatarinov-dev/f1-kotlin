package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.User
import ru.project.f1.service.UserService
import ru.project.f1.utils.SecurityUtils.Companion.encoded
import ru.project.f1.utils.UiUtils.Companion.failBox
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.utils.UiUtils.Companion.successBox
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar


@Route("register")
@Component
@PageTitle("F1 | Register")
@PreserveOnRefresh
@UIScope
class RegisterView : KComposite() {

    @Autowired
    private lateinit var userService: UserService
    private lateinit var loginField: TextField
    private lateinit var passwordField: PasswordField

    var root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "17%"
                h2("Register")
                loginField = textField("Login:") {
                    setWidthFull()
                    placeholder = "Enter login"
                }
                passwordField = passwordField("Password:") {
                    setWidthFull()
                    placeholder = "Enter password"
                }
                button("Register") {
                    setWidthFull()
                    setPrimary()
                    onLeftClick { register() }
                }
            }
        }
    }

    fun register() {
        if (loginField.isEmpty || passwordField.isEmpty) {
            failBox("At least one of fields is empty")
        } else {
            userService.findByLogin(loginField.value).ifPresentOrElse({
                failBox("User with this username is already registered")
            }, {
                val encodedPassword = passwordField.value.encoded()
                val user = User(loginField.value, encodedPassword)
                userService.save(user)
                loginField.clear()
                passwordField.clear()
                successBox("Registration is successfully ended")
                setLocation("/login")
            })
        }
    }
}