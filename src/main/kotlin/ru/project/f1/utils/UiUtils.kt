package ru.project.f1.utils

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Shortcuts
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.server.Command

class UiUtils {

    companion object {

        fun customDialog(dialogMessage: String, action: Button.() -> Unit) {
            val dialog = Dialog().apply {
                height = "20%"
                isCloseOnEsc = false
                isCloseOnOutsideClick = false
                Shortcuts.addShortcutListener(this, Command { close() }, Key.ESCAPE)
                horizontalLayout {
                    p(dialogMessage)
                    height = "75%"
                    alignItems = FlexComponent.Alignment.CENTER
                }
                horizontalLayout {
                    horizontalLayout {
                        button("Cancel") {
                            onLeftClick {
                                close()
                            }
                        }
                        width = "75%"
                    }
                    horizontalLayout {
                        button("Confirm") {
                            setPrimary()
                            onLeftClick {
                                action()
                                close()
                            }
                        }
                    }
                }
            }
            dialog.open()
        }

        fun reload() = UI.getCurrent().page.reload()

        fun setLocation(url: String) {
            UI.getCurrent().page.setLocation(url)
        }

        fun navigate(url: String) {
            UI.getCurrent().navigate(url)
        }

        fun show(message: String) {
            val dialog = Dialog()
            dialog.add(message)
            dialog.open()
        }
    }
}