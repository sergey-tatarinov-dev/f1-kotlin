package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.GrandPrix
import java.math.BigInteger
import java.util.*

interface GrandPrixService {

    @Transactional
    fun save(grandPrix: GrandPrix): GrandPrix
    fun findById(id: BigInteger): Optional<GrandPrix>

    @Transactional
    fun deleteById(id: BigInteger)
    fun findAll(pageable: Pageable): Page<GrandPrix>
    fun findGrandPrixById(id: BigInteger): Optional<GrandPrix>
    fun findAllGrandPrixByYear(year: Int): List<GrandPrix>
    fun findAllYears(): List<Int>
}