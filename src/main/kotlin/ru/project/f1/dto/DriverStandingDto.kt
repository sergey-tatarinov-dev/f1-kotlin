package ru.project.f1.dto

import ru.project.f1.entity.Driver

data class DriverStandingDto(
    var driver: Driver,
    override var totalPoints: Double,
    var grandPrixPoints: Map<Int, PointsPerGrandPrixDto>
): StandingDto(totalPoints)
