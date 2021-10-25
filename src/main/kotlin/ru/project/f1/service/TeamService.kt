package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.Team
import java.math.BigInteger
import java.util.*

interface TeamService {

    @Transactional
    fun save(team: Team): Team

    fun findById(id: BigInteger): Optional<Team>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<Team>
}