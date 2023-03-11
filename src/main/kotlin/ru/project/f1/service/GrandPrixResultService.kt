package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.GrandPrixResult
import ru.project.f1.entity.GrandPrixResultPerDriver
import ru.project.f1.entity.GrandPrixResultPerGrandPrix
import ru.project.f1.entity.Team
import java.math.BigInteger
import java.util.*

interface GrandPrixResultService {

    @Transactional
    fun save(grandPrixResult: GrandPrixResult): GrandPrixResult

    fun findById(id: BigInteger): Optional<GrandPrixResult>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<GrandPrixResult>

    fun findDistinctByGrandPrixId(): List<BigInteger>

    fun findAllByGrandPrixId(id: Int): List<GrandPrixResultPerGrandPrix>

    fun findAllByDriverId(id: Int, year: Int): List<GrandPrixResultPerDriver>

    fun findTeamByDriverIdAndYear(id: BigInteger, year: Int): Team

    fun findAllGrandPrixResultByYear(year: Int): List<GrandPrixResult>
}