package ru.project.f1.dto

import ru.project.f1.entity.GrandPrix

data class PointsPerGrandPrixDto(
    var grandPrix: GrandPrix,
    var points: Double
)
