package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.Comment
import ru.project.f1.entity.News
import java.math.BigInteger
import java.util.*

interface CommentService {

    @Transactional
    fun save(comment: Comment): Comment

    fun findById(id: BigInteger): Optional<Comment>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<Comment>

    fun findAllByNews(news: News, pageable: Pageable): Page<Comment>

    fun countAllByNews(news: News): Long
}