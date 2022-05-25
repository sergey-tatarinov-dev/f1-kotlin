package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.TeamStanding
import ru.project.f1.repository.TeamStandingsRepository
import ru.project.f1.service.TeamStandingsService
import java.util.*

@Service
class TeamStandingsServiceImpl : TeamStandingsService {

    @Autowired
    private lateinit var teamStandingsRepository: TeamStandingsRepository

    override fun save(teamStanding: TeamStanding): TeamStanding = teamStandingsRepository.save(teamStanding)

    override fun findById(id: Int): Optional<TeamStanding> = teamStandingsRepository.findById(id)

    override fun deleteById(id: Int) = teamStandingsRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<TeamStanding> = teamStandingsRepository.findAll(pageable)
}