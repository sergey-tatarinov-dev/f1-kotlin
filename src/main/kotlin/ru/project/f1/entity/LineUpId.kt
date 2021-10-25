package ru.project.f1.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class LineUpId(
    var teamId: Int,
    var driverId: Int,
): Serializable
