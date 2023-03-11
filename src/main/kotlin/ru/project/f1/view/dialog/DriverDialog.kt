package ru.project.f1.view.dialog

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.Action
import ru.project.f1.entity.Country
import ru.project.f1.entity.Driver
import ru.project.f1.service.CountryService
import ru.project.f1.service.DriverService
import ru.project.f1.utils.UiUtils.Companion.notNull
import ru.project.f1.utils.UiUtils.Companion.successBox
import ru.project.f1.view.fragment.Components.Companion.integerField1
import ru.project.f1.view.fragment.Components.Companion.textField1
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Component
@UIScope
class DriverDialog: ConfirmDialog() {

    @Autowired
    private lateinit var countryService: CountryService
    @Autowired
    private lateinit var driverService: DriverService
    @Autowired
    private lateinit var countryDialog: CountryDialog
    private lateinit var driverNameTextField: TextField
    private lateinit var driverSurnameTextField: TextField
    private lateinit var driverRaceNumberField: IntegerField
    private lateinit var countrySelect: Select<Country>
    private lateinit var consumer: Consumer<Driver>
    private var action: Action? = null
    private var editableDriver: Driver? = null
    private var savedDriver: Driver? = null

    @PostConstruct
    fun init() {
        add(content())
    }

    fun content(): VerticalLayout {
        return confirmDialog("Add a Driver", {
            driverNameTextField = textField1("Driver name", "Enter driver's name", {
                saveButton.isEnabled = allFieldsNotNull()
            })
            driverSurnameTextField = textField1("Driver surname", "Enter driver's surname", {
                saveButton.isEnabled = allFieldsNotNull()
            })
            driverRaceNumberField = integerField1 ("Race number", "Enter driver's race number", {
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
            val driver = Driver(
                name = driverNameTextField.value,
                surname = driverSurnameTextField.value,
                raceNumber = driverRaceNumberField.value.toInt(),
                country = countrySelect.value
            )
            if (action == Action.EDIT) {
                driver.apply {
                    id = editableDriver!!.id
                }
            }
            savedDriver = driverService.save(driver)
            consumer.accept(savedDriver!!)
            successBox("Driver has been successfully ${if (action == Action.EDIT) "edited" else "saved"}")
            close()
        })
    }

    fun allFieldsNotNull(): Boolean =
        driverNameTextField.notNull() && driverSurnameTextField.notNull() && driverRaceNumberField.notNull() && countrySelect.notNull()

    fun openAndThen(act: Action, driver: Driver? = null, cons: Consumer<Driver>) {
        action = act
        if (action == Action.EDIT) {
            driverNameTextField.value = driver?.name
            driverSurnameTextField.value = driver?.surname
            driverRaceNumberField.value = driver?.raceNumber
            countrySelect.value = driver?.country
        }
        editableDriver = driver
        consumer = cons
        open()
    }

    override fun close() {
        super.close()
        driverNameTextField.clear()
        driverSurnameTextField.clear()
        driverRaceNumberField.clear()
        countrySelect.clear()
    }
}