package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.project.f1.entity.DriverStanding

@Repository
interface DriverStandingsRepository: JpaRepository<DriverStanding, Int> {
}