package ru.project.f1.view.fragment

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.github.mvysny.karibudsl.v10.numberField
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

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

    }
}