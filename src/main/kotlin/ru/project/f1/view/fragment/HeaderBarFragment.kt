package ru.project.f1.view.fragment

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.notification.Notification.show
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import ru.project.f1.utils.SecurityUtils
import ru.project.f1.utils.UiUtils.Companion.setLocation

class HeaderBarFragment {

    companion object {

        fun (@VaadinDsl Button).setError() {
            addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY)
        }

        @VaadinDsl
        fun (@VaadinDsl HasComponents).loginHeaderBar(block: HorizontalLayout.() -> Unit = {}): HorizontalLayout {
            val layout: HorizontalLayout = HorizontalLayout().apply {
                setWidthFull()
                navBar { }
            }
            return init(layout, block)
        }

        @VaadinDsl
        fun (@VaadinDsl HasComponents).navBar(block: MenuBar.() -> Unit = {}): MenuBar {
            val menuBar: MenuBar = MenuBar().apply {
                setSizeFull()
                addItem("News") {
                    show("News")
                    setLocation("/news")
                }
                addItem("Drivers") {
                    show("Drivers")
                    setLocation("/drivers")
                }
                addItem("Grand Prix") {
                    show("Grand Prix")
                    setLocation("/grand-prix")
                }
                addItem("Drivers Standings") {
                    show("Drivers Standings")
                    setLocation("/drivers-standings")
                }
                addItem("Constructor Standings") {
                    show("Constructor Standings")
                    setLocation("/constructor-standings")
                }
            }
            return init(menuBar, block)
        }

        @VaadinDsl
        fun (@VaadinDsl HasComponents).title(label: String = "", block: (@VaadinDsl H1).() -> Unit = {}): H1 {
            val h1: H1 = h1(label).apply {
                style.set("margin-top", "0px")
                style.set("margin-bottom", "0px")
                style.set("flex-grow", "1")
            }
            return init(h1, block)
        }

        @VaadinDsl
        fun (@VaadinDsl HasComponents).headerBar(block: HorizontalLayout.() -> Unit = {}): HorizontalLayout {
            val layout: HorizontalLayout = HorizontalLayout().apply {
                setWidthFull()
                navBar { }
                menuBar {
                    if (SecurityUtils.isUserLoggedIn()) {
                        val login = SecurityUtils.getUser().login
                        addItem(login) {
                            setLocation("/profile/${login}")
                        }
                        addItem("Log out") {
                            setLocation("/logout")
                        }
                    } else {
                        addItem("Login") {
                            show("Login")
                            setLocation("/login")
                        }
                    }
                }
            }
            return init(layout, block)
        }
    }
}