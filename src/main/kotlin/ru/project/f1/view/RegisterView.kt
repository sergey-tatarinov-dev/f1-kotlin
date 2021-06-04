package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import ru.project.f1.entity.User
import ru.project.f1.service.UserService
import ru.project.f1.view.fragment.HeaderBarView.Companion.headerBar
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.utils.UiUtils.Companion.show


@Route("register")
@Component
@PageTitle("Register | F1")
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
                    width = "100%"
                    placeholder = "Enter login"
                }
                passwordField = passwordField("Password:") {
                    width = "100%"
                    placeholder = "Enter password"
                }
                button("Register") {
                    width = "100%"
                    setPrimary()
                    onLeftClick { register() }
                }
            }
        }
    }

    fun register() {
        if (loginField.isEmpty || passwordField.isEmpty) {
            show("At least one of fields is empty")
        } else {
            userService.findByLogin(loginField.value).ifPresentOrElse({
                show("User with this username is already registered")
            }, {
                val encodedPassword = BCryptPasswordEncoder(12).encode(passwordField.value)
                val user = User(loginField.value, encodedPassword)
                userService.save(user)
                loginField.clear()
                passwordField.clear()
                show("Registration is successfully ended")
                setLocation("/login")
            })
        }
    }
}