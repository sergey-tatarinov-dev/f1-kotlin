package ru.project.f1.view.dialog

import com.github.mvysny.karibudsl.v10.upload
import com.github.mvysny.kaributools.clear
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.dom.DomEventListener
import com.vaadin.flow.spring.annotation.UIScope
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.entity.Country
import ru.project.f1.service.CountryService
import ru.project.f1.service.FileService
import ru.project.f1.utils.UiUtils.Companion.notNull
import ru.project.f1.view.fragment.Components.Companion.textField1
import java.io.File
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Component
@UIScope
class CountryDialog : ConfirmDialog() {

    @Autowired
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var countryService: CountryService
    private lateinit var countryTextField: TextField
    private lateinit var upload: Upload
    private lateinit var fileName: String
    private lateinit var consumer: Consumer<Country>
    private var fileContent: ByteArray? = null
    private var buffer = MultiFileMemoryBuffer()
    private var savedCountry: Country? = null

    @PostConstruct
    fun init() {
        add(content())
    }

    fun content(): VerticalLayout {
        return confirmDialog("Add a country", {
            countryTextField = textField1("Country name", "Enter country name", {
                saveButton.isEnabled = allFieldsNotNull()
            })
            upload = upload(buffer, block = {
                width = "90%"
                setAcceptedFileTypes(".jpg", ".jpeg", ".png", ".bmp")
                addSucceededListener {
                    fileName = it.fileName
                    fileContent = IOUtils.toByteArray(buffer.getInputStream(fileName))
                    saveButton.isEnabled = allFieldsNotNull()
                }
                addFileRemoveListener {
                    fileContent = null
                    saveButton.isEnabled = false
                }
            })
        }, {
            val file = File(fileName)
            val savedFile = fileService.save(file, countryTextField.value, fileContent!!)
            savedCountry = countryService.save(Country(name = countryTextField.value, f1File = savedFile))
            consumer.accept(savedCountry!!)
        })
    }

    fun openAndThen(cons: Consumer<Country>) {
        consumer = cons
        open()
    }

    override fun close() {
        super.close()
        fileContent = null
        countryTextField.clear()
        upload.clear()
    }

    fun Upload.addFileRemoveListener(listener: DomEventListener) {
        element.addEventListener("file-remove", listener)
            .addEventData("event.detail.file.name")
    }

    fun allFieldsNotNull(): Boolean =
        countryTextField.notNull() && fileContent != null
}