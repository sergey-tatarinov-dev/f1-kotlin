package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.GrandPrix
import ru.project.f1.repository.GrandPrixRepository
import ru.project.f1.service.GrandPrixService
import java.math.BigInteger
import java.util.*

@Service
class GrandPrixServiceImpl : GrandPrixService {

    @Autowired
    private lateinit var grandPrixRepository: GrandPrixRepository

    override fun save(grandPrix: GrandPrix): GrandPrix = grandPrixRepository.save(grandPrix)

    override fun findById(id: BigInteger): Optional<GrandPrix> = grandPrixRepository.findById(id)

    override fun deleteById(id: BigInteger) = grandPrixRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<GrandPrix> = grandPrixRepository.findAll(pageable)

    override fun findAllYears(): List<Int> = grandPrixRepository.findAllYears()

    override fun findGrandPrixById(id: BigInteger): Optional<GrandPrix> = grandPrixRepository.findById(id)

    override fun findAllGrandPrixByYear(year: Int): List<GrandPrix> = grandPrixRepository.findAllGrandPrixByYear(year)
}