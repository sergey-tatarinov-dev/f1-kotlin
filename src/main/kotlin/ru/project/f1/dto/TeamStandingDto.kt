package ru.project.f1.dto

import ru.project.f1.entity.Team

data class TeamStandingDto(
    var team: Team,
    override var totalPoints: Double,
    var grandPrixPoints: List<PointsPerGrandPrixDto>
):StandingDto(totalPoints)
