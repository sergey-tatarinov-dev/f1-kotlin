package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.Track
import java.math.BigInteger
import java.util.*

interface TrackService {

    @Transactional
    fun save(track: Track): Track

    fun findById(id: BigInteger): Optional<Track>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<Track>
}