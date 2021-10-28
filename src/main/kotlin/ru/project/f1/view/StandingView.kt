package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.KComposite
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.io.FileInputStream

open class StandingView : KComposite() {

    fun imageFromPath(src: String, alt: String) =
        Image(
            StreamResource(alt,
                InputStreamFactory {
                    FileInputStream(src)
                }), alt
        ).apply {
            setWidthFull()
            height = "20px"
        }
}