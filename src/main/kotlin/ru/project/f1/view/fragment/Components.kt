package ru.project.f1.view.fragment

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.karibudsl.v10.numberField
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import ru.project.f1.utils.UiUtils

class Components {

    companion object {

        @VaadinDsl
        fun (@VaadinDsl HasComponents).textField1(
            label: String = "",
            placeholder: String = "",
            valueChangeListener: (event: AbstractField.ComponentValueChangeEvent<TextField, String>) -> Unit = {},
            block: (@VaadinDsl TextField).() -> Unit = {}
        ): TextField {
            val textField: TextField = textField(label).apply {
                setWidthFull()
                valueChangeMode = ValueChangeMode.EAGER
                this.placeholder = placeholder
                addValueChangeListener(valueChangeListener)
            }
            return init(textField, block)
        }

        @VaadinDsl
        fun (@VaadinDsl HasComponents).numberField1(
            label: String = "",
            placeholder: String = "",
            valueChangeListener: (event: AbstractField.ComponentValueChangeEvent<NumberField, Double>) -> Unit = {},
            block: (@VaadinDsl NumberField).() -> Unit = {}
        ): NumberField {
            val numberField: NumberField = numberField(label).apply {
                setWidthFull()
                valueChangeMode = ValueChangeMode.EAGER
                this.placeholder = placeholder
                addValueChangeListener(valueChangeListener)
            }
            return init(numberField, block)
        }

        fun createParagraph(value: String): Paragraph {
            return Paragraph(value).apply {
                style.apply {
                    set("font-size", "18px")
                    set("line-height", "0.5")
                }
                setWidthFull()
            }
        }

        fun createRemoveButton(action: Icon.() -> Unit): Icon {
            return createButton(VaadinIcon.CLOSE) {
                UiUtils.customDialog("Are you sure you want to delete the comment?") {
                    action()
                    UiUtils.reload()
                }
            }
        }

        fun createEditButton(action: Icon.() -> Unit): Icon {
            return createButton(VaadinIcon.PENCIL) {
                action()
            }
        }

        private fun createButton(vaadinIcon: VaadinIcon, action: Icon.() -> Unit): Icon {
            return Icon(vaadinIcon).apply {
                style.apply {
                    set("height", "14px")
                    set("padding-top", "10px")
                    set("cursor", "pointer")
                }
                addClickListener {
                    action()
                }
            }
        }
    }
}