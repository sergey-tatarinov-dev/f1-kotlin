package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.Driver
import ru.project.f1.repository.DriverRepository
import ru.project.f1.service.DriverService
import java.math.BigInteger

@Service
class DriverServiceImpl : DriverService {

    @Autowired
    private lateinit var driverRepository: DriverRepository

    override fun save(driver: Driver): Driver = driverRepository.save(driver)

    override fun findById(id: BigInteger) = driverRepository.findById(id)

    override fun deleteById(id: BigInteger) = driverRepository.deleteById(id)

    override fun findAllDriversByYear(year: Int): List<Driver> = driverRepository.findAllDriversByYear(year)

    override fun findAll(pageable: Pageable) = driverRepository.findAll(pageable)

    override fun findAllDriverYears(): List<Int> = driverRepository.findAllDriverYears()
}