package ru.project.f1.view.fragment

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.karibudsl.v10.menuBar
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import ru.project.f1.utils.SecurityUtils
import ru.project.f1.utils.UiUtils

class HeaderBarView {

    companion object {
        @VaadinDsl
        fun (@VaadinDsl HasComponents).loginHeaderBar(block: HorizontalLayout.() -> Unit = {}): HorizontalLayout {
            val layout: HorizontalLayout = HorizontalLayout().apply {
                width = "100%"
                navBar { }
            }
            return init(layout, block)
        }

        @VaadinDsl
        fun (@VaadinDsl HasComponents).navBar(block: MenuBar.() -> Unit = {}): MenuBar {
            val menuBar: MenuBar = MenuBar().apply {
                setSizeFull()
                addItem("News") {
                    Notification.show("News")
                    UiUtils.setLocation("/news")
                }
                addItem("Constructor Standings") {
                    Notification.show("Constructor Standings")
                    UiUtils.setLocation("/constructor-standings")
                }
                addItem("Driver Standings") {
                    Notification.show("Driver Standings")
                    UiUtils.setLocation("/driver-standings")
                }
            }
            return init(menuBar, block)
        }

        @VaadinDsl
        fun (@VaadinDsl HasComponents).headerBar(block: HorizontalLayout.() -> Unit = {}): HorizontalLayout {
            val layout: HorizontalLayout = HorizontalLayout().apply {
                width = "100%"
                navBar { }
                menuBar {
                    if (SecurityUtils.isUserLoggedIn()) {
                        val login = SecurityUtils.getUser().login
                        addItem(login) {
                            UiUtils.setLocation("/profile/${login}")
                        }
                        addItem("Log out") {
                            UiUtils.setLocation("/logout")
                        }
                    } else {
                        addItem("Login") {
                            Notification.show("Login")
                            UiUtils.setLocation("/login")
                        }
                    }
                }
            }
            return init(layout, block)
        }
    }
}