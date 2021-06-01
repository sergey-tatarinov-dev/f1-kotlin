package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.User
import ru.project.f1.service.UserService

@Route("welcome-login")
@Component
@PWA(name = "F1 News", shortName = "MyPWA")
class LoginView : KComposite() {

    @Autowired
    private lateinit var userService: UserService

    val root = ui {
        verticalLayout {
            horizontalLayout {
                setSizeFull()
                val navBar = menuBar {
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
                val loginMenuBar = menuBar {
                    addItem("Login") {
                        Notification.show("Login")
                        UI.getCurrent().navigate("welcome-login")
                    }
                }
            }
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            alignItems = FlexComponent.Alignment.CENTER
            formLayout {
                width = "50%"
                val loginField = textField("Login:") {
                    placeholder = "Last name, first name"
                }
                val passwordField = passwordField("Password:") {
                    width = "5em"
                    addKeyDownListener(::println)
                }
                button("Register") {
                    onLeftClick {
                        if (loginField.isEmpty || passwordField.isEmpty) {
                            val dialog = Dialog()
                            dialog.add("At least one of fields is empty")
                            dialog.open()
                        } else {
                            val user = User(login = loginField.value, password = passwordField.value)
                            userService.save(user)
                            loginField.clear()
                            passwordField.clear()
                        }
                    }
                }
                button("Login") {
                    setPrimary()
                    onLeftClick {
                        if (loginField.isEmpty || passwordField.isEmpty) {
                            val dialog = Dialog()
                            dialog.add("At least one of fields is empty")
                            dialog.open()
                        } else {
                            userService.findByLoginAndPassword(loginField.value, passwordField.value).ifPresent {
                                loginField.clear()
                                passwordField.clear()
                                UI.getCurrent().navigate("news")
                            }
                        }
                    }
                }
            }
        }
    }
}