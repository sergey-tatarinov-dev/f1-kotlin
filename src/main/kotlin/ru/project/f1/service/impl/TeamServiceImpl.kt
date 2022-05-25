package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.Team
import ru.project.f1.repository.TeamRepository
import ru.project.f1.service.TeamService
import java.math.BigInteger

@Service
class TeamServiceImpl : TeamService {

    @Autowired
    private lateinit var teamRepository: TeamRepository

    override fun save(team: Team): Team = teamRepository.save(team)

    override fun findById(id: BigInteger) = teamRepository.findById(id)

    override fun deleteById(id: BigInteger) = teamRepository.deleteById(id)

    override fun findAll(pageable: Pageable) = teamRepository.findAll(pageable)
}