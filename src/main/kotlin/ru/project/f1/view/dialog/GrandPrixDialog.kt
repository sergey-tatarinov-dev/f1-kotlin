package ru.project.f1.view.dialog

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.*
import ru.project.f1.service.GrandPrixService
import ru.project.f1.service.TrackService
import ru.project.f1.utils.UiUtils.Companion.notNull
import ru.project.f1.utils.UiUtils.Companion.successBox
import ru.project.f1.view.fragment.Components.Companion.textField1
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Component
@UIScope
class GrandPrixDialog : ConfirmDialog() {

    @Autowired
    private lateinit var trackService: TrackService

    @Autowired
    private lateinit var grandPrixService: GrandPrixService

    @Autowired
    private lateinit var trackDialog: TrackDialog
    private lateinit var grandPrixNameTextField: TextField
    private lateinit var trackSelect: Select<Track>
    private lateinit var grandPrixDatePicker: DatePicker
    private lateinit var consumer: Consumer<GrandPrix>
    private var action: Action? = null
    private var savedGrandPrix: GrandPrix? = null
    private var editableGrandPrix: GrandPrix? = null

    @PostConstruct
    fun init() {
        add(content())
    }

    fun content(): VerticalLayout {
        return confirmDialog("Add a Grand Prix", {
            grandPrixNameTextField = textField1("Grand Prix name", "Enter grand prix name", {
                saveButton.isEnabled = allFieldsNotNull()
            })
            horizontalLayout {
                setWidthFull()
                trackSelect = select("Track") {
                    setWidthFull()
                    placeholder = "Choose a track"
                    setItems(trackService.findAll(PageRequest.of(0, 50)).toMutableList())
                    setItemLabelGenerator(Track::circuitName)
                    addValueChangeListener {
                        saveButton.isEnabled = allFieldsNotNull()
                    }
                }
                button("+") {
                    onLeftClick {
                        trackDialog.openAndThen {
                            trackSelect.setItems(trackService.findAll(PageRequest.of(0, 50)).toMutableList())
                            trackSelect.value = it
                        }
                    }
                }
            }
            grandPrixDatePicker = datePicker("Grand Prix date") {
                setWidthFull()
                placeholder = "Pick a Grand Prix date"
                addValueChangeListener {
                    saveButton.isEnabled = allFieldsNotNull()
                }
            }
        }, {
            val grandPrix = GrandPrix(
                date = grandPrixDatePicker.value,
                fullName = grandPrixNameTextField.value,
                track = trackSelect.value
            )
            if (action == Action.EDIT) {
                grandPrix.apply {
                    id = editableGrandPrix!!.id
                    grandPrixOver = editableGrandPrix!!.grandPrixOver
                    deleted = editableGrandPrix!!.deleted
                    sprint = editableGrandPrix!!.sprint
                    needApplyHalfPointsMultiplier = editableGrandPrix!!.needApplyHalfPointsMultiplier
                }
            }
            savedGrandPrix = grandPrixService.save(grandPrix)
            consumer.accept(savedGrandPrix!!)
            successBox("Grand Prix has been successfully ${if (action == Action.EDIT) "edited" else "saved"}")
            close()
        })
    }

    fun allFieldsNotNull(): Boolean =
        grandPrixNameTextField.notNull() && trackSelect.notNull() && grandPrixDatePicker.notNull()

    override fun close() {
        super.close()
        grandPrixNameTextField.clear()
        trackSelect.clear()
        grandPrixDatePicker.clear()
    }

    fun openAndThen(act: Action, grandPrix: GrandPrix? = null, cons: Consumer<GrandPrix>) {
        action = act
        if (action == Action.EDIT) {
            grandPrixNameTextField.value = grandPrix?.fullName
            grandPrixDatePicker.value = grandPrix?.date
            trackSelect.value = grandPrix?.track
        }
        editableGrandPrix = grandPrix
        consumer = cons
        open()
    }
}