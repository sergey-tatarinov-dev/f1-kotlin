package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.TeamStanding
import java.util.*


interface TeamStandingsService {

    @Transactional
    fun save(teamStanding: TeamStanding): TeamStanding

    fun findById(id: Int): Optional<TeamStanding>

    @Transactional
    fun deleteById(id: Int)

    fun findAll(pageable: Pageable): Page<TeamStanding>
}