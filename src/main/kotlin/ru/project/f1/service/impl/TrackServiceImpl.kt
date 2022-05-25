package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.Track
import ru.project.f1.repository.TrackRepository
import ru.project.f1.service.TrackService
import java.math.BigInteger

@Service
class TrackServiceImpl : TrackService {

    @Autowired
    private lateinit var trackRepository: TrackRepository

    override fun save(track: Track): Track = trackRepository.save(track)

    override fun findById(id: BigInteger) = trackRepository.findById(id)

    override fun deleteById(id: BigInteger) = trackRepository.deleteById(id)

    override fun findAll(pageable: Pageable) = trackRepository.findAll(pageable)
}