package ru.project.f1.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.project.f1.entity.Comment
import ru.project.f1.entity.News
import java.math.BigInteger

@Repository
interface CommentRepository : JpaRepository<Comment, BigInteger> {

    fun findAllByNews(news: News, pageable: Pageable): Page<Comment>

    fun countAllByNews(news: News): Long
}