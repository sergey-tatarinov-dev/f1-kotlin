package ru.project.f1.view.dialog

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.select
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.Country
import ru.project.f1.entity.Track
import ru.project.f1.service.CountryService
import ru.project.f1.service.TrackService
import ru.project.f1.utils.UiUtils.Companion.notNull
import ru.project.f1.view.fragment.Components.Companion.numberField1
import ru.project.f1.view.fragment.Components.Companion.textField1
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Component
@UIScope
class TrackDialog : ConfirmDialog() {

    @Autowired
    private lateinit var countryDialog: CountryDialog

    @Autowired
    private lateinit var countryService: CountryService

    @Autowired
    private lateinit var trackService: TrackService
    private lateinit var circuitNameTextField: TextField
    private lateinit var trackLengthNumberField: NumberField
    private lateinit var lapCountNumberField: NumberField
    private lateinit var countrySelect: Select<Country>
    private lateinit var consumer: Consumer<Track>
    private var savedTrack: Track? = null

    @PostConstruct
    fun init() {
        add(content())
    }

    fun content(): VerticalLayout {
        return confirmDialog("Add a Track", {
            circuitNameTextField = textField1("Circuit name", "Enter circuit name", {
                saveButton.isEnabled = allFieldsNotNull()
            })
            trackLengthNumberField = numberField1("Track length", "Enter track length", {
                saveButton.isEnabled = allFieldsNotNull()
            })
            lapCountNumberField = numberField1("Lap count", "Enter lap count", {
                saveButton.isEnabled = allFieldsNotNull()
            })
            horizontalLayout {
                setWidthFull()
                countrySelect = select("Country") {
                    setWidthFull()
                    placeholder = "Choose a country"
                    setItems(countryService.findAll(PageRequest.of(0, 100)).toMutableList())
                    setItemLabelGenerator(Country::name)
                    addValueChangeListener {
                        saveButton.isEnabled = allFieldsNotNull()
                    }
                }
                button("+") {
                    onLeftClick {
                        countryDialog.openAndThen {
                            countrySelect.setItems(countryService.findAll(PageRequest.of(0, 100)).toMutableList())
                            countrySelect.value = it
                        }
                    }
                }
            }
        }, {
            val totalMileage: Double = lapCountNumberField.value * trackLengthNumberField.value
            val range = 305_000f..310_000f
            if (totalMileage in range) {
                savedTrack = trackService.save(
                    Track(
                        circuitName = circuitNameTextField.value,
                        length = trackLengthNumberField.value.toInt(),
                        lapCount = lapCountNumberField.value.toInt(),
                        country = countrySelect.value
                    )
                )
                consumer.accept(savedTrack!!)
            }
        })
    }

    fun allFieldsNotNull(): Boolean =
        circuitNameTextField.notNull() && trackLengthNumberField.notNull() &&
                lapCountNumberField.notNull() && countrySelect.notNull()

    fun openAndThen(cons: Consumer<Track>) {
        consumer = cons
        open()
    }

    override fun close() {
        super.close()
        circuitNameTextField.clear()
        trackLengthNumberField.clear()
        lapCountNumberField.clear()
        countrySelect.clear()
    }
}