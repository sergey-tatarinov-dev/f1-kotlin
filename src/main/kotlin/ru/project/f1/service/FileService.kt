package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.F1File
import java.io.File
import java.math.BigInteger
import java.util.*

interface FileService {

    @Transactional
    fun save(file: File): F1File

    fun findById(id: BigInteger): Optional<File>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<F1File>
}