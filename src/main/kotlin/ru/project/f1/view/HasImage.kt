package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.KComposite
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.project.f1.service.FileService
import java.io.FileInputStream
import java.math.BigInteger

@Component
class HasImage : KComposite() {

    @Autowired
    lateinit var fileService: FileService

    fun imageById(id: BigInteger, alt: String, block: Image.() -> Unit = {}): Image {
        val file = fileService.findById(id).orElseThrow()
        return imageFromPath(file.absolutePath, alt, block);
    }

    private fun imageFromPath(src: String, alt: String, block: Image.() -> Unit = {}): Image {
        return Image(streamResourceFrom(src, alt), alt).apply {
            setWidthFull()
            block()
        }
    }

    fun avatarById(id: BigInteger, alt: String, block: Avatar.() -> Unit = {}): Avatar {
        val file = fileService.findById(id).orElseThrow()
        return avatarFromPath(file.absolutePath, alt, block)
    }

    private fun avatarFromPath(src: String, name: String, block: Avatar.() -> Unit = {}): Avatar =
        Avatar(name).apply {
            imageResource = streamResourceFrom(src, name)
            setWidthFull()
            block()
        }

    private fun streamResourceFrom(src: String, name: String) =
        StreamResource(name,
            InputStreamFactory {
                FileInputStream(src)
            })
}