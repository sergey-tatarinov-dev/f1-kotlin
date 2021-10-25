package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.DriverStanding
import java.util.*


interface DriverStandingsService {

    @Transactional
    fun save(driverStanding: DriverStanding): DriverStanding

    fun findById(id: Int): Optional<DriverStanding>

    @Transactional
    fun deleteById(id: Int)

    fun findAll(pageable: Pageable): Page<DriverStanding>
}