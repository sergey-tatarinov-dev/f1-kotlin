package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.DriverStanding
import ru.project.f1.repository.DriverStandingsRepository
import ru.project.f1.service.DriverStandingsService
import java.util.*

@Service
class DriverStandingsServiceImpl : DriverStandingsService {

    @Autowired
    private lateinit var driverStandingsRepository: DriverStandingsRepository

    override fun save(driverStanding: DriverStanding): DriverStanding = driverStandingsRepository.save(driverStanding)

    override fun findById(id: Int): Optional<DriverStanding> = driverStandingsRepository.findById(id)

    override fun deleteById(id: Int) = driverStandingsRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<DriverStanding> = driverStandingsRepository.findAll(pageable)
}