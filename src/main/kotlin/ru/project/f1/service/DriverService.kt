package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.Driver
import java.math.BigInteger
import java.util.*

interface DriverService {

    @Transactional
    fun save(driver: Driver): Driver

    fun findById(id: BigInteger): Optional<Driver>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<Driver>
}