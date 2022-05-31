package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.Country
import java.math.BigInteger
import java.util.*

interface CountryService {

    @Transactional
    fun save(country: Country): Country

    fun findById(id: BigInteger): Optional<Country>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<Country>
}