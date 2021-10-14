package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.Comment
import ru.project.f1.entity.News
import ru.project.f1.repository.CommentRepository
import ru.project.f1.service.CommentService
import java.math.BigInteger
import java.util.*

@Service
class CommentServiceImpl : CommentService {

    @Autowired
    private lateinit var commentRepository: CommentRepository

    override fun save(comment: Comment): Comment = commentRepository.save(comment)

    override fun findById(id: BigInteger): Optional<Comment> = commentRepository.findById(id)

    override fun deleteById(id: BigInteger) = commentRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<Comment> = commentRepository.findAll(pageable)

    override fun findAllByNews(news: News, pageable: Pageable): Page<Comment> =
        commentRepository.findAllByNews(news, pageable)

    override fun countAllByNews(news: News): Long = commentRepository.countAllByNews(news)

}