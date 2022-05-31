package ru.project.f1.utils

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.p
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Shortcuts
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
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

        fun NumberField.notNull(): Boolean = this.value != null
        fun TextField.notNull(): Boolean = this.value != null && this.value.isNotEmpty()
        fun <T> Select<T>.notNull(): Boolean = this.value != null
        fun DatePicker.notNull(): Boolean = this.value != null
    }
}