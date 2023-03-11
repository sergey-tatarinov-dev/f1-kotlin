package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.GrandPrixResult
import ru.project.f1.entity.GrandPrixResultPerDriver
import ru.project.f1.entity.GrandPrixResultPerGrandPrix
import ru.project.f1.entity.Team
import ru.project.f1.repository.GrandPrixResultRepository
import ru.project.f1.service.GrandPrixResultService
import java.math.BigInteger
import java.util.*

@Service
class GrandPrixResultServiceImpl : GrandPrixResultService {

    @Autowired
    private lateinit var grandPrixResultRepository: GrandPrixResultRepository

    override fun save(grandPrixResult: GrandPrixResult): GrandPrixResult =
        grandPrixResultRepository.save(grandPrixResult)

    override fun findById(id: BigInteger): Optional<GrandPrixResult> = grandPrixResultRepository.findById(id)

    override fun deleteById(id: BigInteger) = grandPrixResultRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<GrandPrixResult> = grandPrixResultRepository.findAll(pageable)

    override fun findDistinctByGrandPrixId(): List<BigInteger> = grandPrixResultRepository.findDistinctByGrandPrixId()

    override fun findAllByGrandPrixId(id: Int): List<GrandPrixResultPerGrandPrix> =
        grandPrixResultRepository.findAllGrandPrixResultByIdAndYear(id)

    override fun findAllByDriverId(id: Int, year: Int): List<GrandPrixResultPerDriver> =
        grandPrixResultRepository.findAllByDriverIdAndYear(id, year)

    override fun findTeamByDriverIdAndYear(id: BigInteger, year: Int): Team = grandPrixResultRepository.findTeamByDriverIdAndYear(id, year)[0]

    override fun findAllGrandPrixResultByYear(year: Int): List<GrandPrixResult> = grandPrixResultRepository.findAllGrandPrixResultByYear(year)

    override fun findAllYears(): List<Int> = grandPrixResultRepository.findAllYears()

}