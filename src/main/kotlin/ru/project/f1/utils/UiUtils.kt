package ru.project.f1.utils

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog

class UiUtils {

    companion object {

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