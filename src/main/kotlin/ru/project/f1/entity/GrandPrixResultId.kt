package ru.project.f1.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class GrandPrixResultId(
    var grandPrixId: Int,
    var driverId: Int,
) : Serializable
