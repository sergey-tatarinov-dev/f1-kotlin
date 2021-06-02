package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.News
import java.math.BigInteger
import java.util.*

interface NewsService {

    @Transactional
    fun save(news: News): News

    fun findById(id: BigInteger): Optional<News>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<News>
}