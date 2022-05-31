package ru.project.f1.view.dialog

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component

@Component
@UIScope
abstract class ConfirmDialog : Dialog() {

    lateinit var saveButton: Button

    fun confirmDialog(
        @NonNull windowTitle: String,
        block: VerticalLayout.() -> Unit = {},
        onSave: () -> Unit = {}
    ): VerticalLayout {
        apply { width = "20%" }
        return verticalLayout {
            style.set("padding", "0px")
            isResizable = true; isModal = false; isDraggable = true
            verticalLayout {
                style.set("padding", "0px")
                h3(windowTitle)

                apply(block)

                horizontalLayout {
                    content { align(right, middle) }
                    setWidthFull()
                    saveButton = button("Save") {
                        isEnabled = false
                        setPrimary()
                        onLeftClick {
                            onSave.invoke()
                            close()
                        }
                    }
                    button("Close") {
                        style.set("margin-left", "auto")
                        onLeftClick {
                            close()
                        }
                    }
                }
            }
        }
    }
}