package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.User
import ru.project.f1.service.UserService
import java.math.BigInteger
import java.security.MessageDigest

@Route("welcome-login")
@Component
@PWA(name = "F1 News", shortName = "F1")
class LoginView : KComposite() {

    @Autowired
    private lateinit var userService: UserService
    private lateinit var loginField: TextField
    private lateinit var passwordField: PasswordField

    val root = ui {
        verticalLayout {
            horizontalLayout {
                width = "100%"
                height = "40%"
                menuBar {
                    setSizeFull()
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
            formLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "40%"
                loginField = textField("Login:") {
                    placeholder = "Last name, first name"
                }
                passwordField = passwordField("Password:") {
                    width = "5em"
                    addKeyDownListener(::println)
                }
                button("Register") {
                    onLeftClick { register() }
                }
                button("Login") {
                    setPrimary()
                    onLeftClick { login() }
                }
            }
        }
    }

    fun login() {
        if (loginField.isEmpty || passwordField.isEmpty) {
            Dialog().show("At least one of fields is empty")
        } else {
            userService.findByLoginAndPassword(loginField.value, passwordField.value.toMD5())
                .ifPresentOrElse({
                    loginField.clear()
                    passwordField.clear()
                    UI.getCurrent().navigate("news")
                }, {
                    Dialog().show("Wrong password or login")
                })
        }
    }

    fun register() {
        if (loginField.isEmpty || passwordField.isEmpty) {
            Dialog().show("At least one of fields is empty")
        } else {
            userService.findByLogin(loginField.value).ifPresentOrElse({
                Dialog().show("User with this username is already registered")
            }, {
                val user = User(loginField.value, passwordField.value.toMD5())
                userService.save(user)
                loginField.clear()
                passwordField.clear()
            })
        }
    }

    fun String.toMD5(): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(this.toByteArray(), 0, this.length)
        return BigInteger(1, messageDigest.digest()).toString(16)
    }

    fun Dialog.show(message: String) {
        val dialog = Dialog()
        dialog.add(message)
        dialog.open()
    }
}