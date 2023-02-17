package ru.project.f1.dto

import ru.project.f1.entity.Team

data class LineUpDto(
    var driver: DriverStandingDto,
    var team: Team,
    var year: Int
)
