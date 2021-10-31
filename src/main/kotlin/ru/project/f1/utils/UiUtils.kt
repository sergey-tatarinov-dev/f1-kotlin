package ru.project.f1.utils

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Shortcuts
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.server.Command
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.io.FileInputStream

class UiUtils {

    companion object {

        fun imageFromPath(src: String, alt: String): Image =
            Image(
                StreamResource(alt,
                    InputStreamFactory {
                        FileInputStream(src)
                    }), alt
            ).apply {
                setWidthFull()
            }

        fun avatarFromPath(src: String, name: String): Avatar =
            Avatar(name).apply {
                imageResource = StreamResource(name, InputStreamFactory { FileInputStream(src) })
                setWidthFull()
            }

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