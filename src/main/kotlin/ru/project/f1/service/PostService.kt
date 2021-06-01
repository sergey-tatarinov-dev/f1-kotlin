package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.Post
import java.math.BigInteger
import java.util.*

interface PostService {

    @Transactional
    fun save(post: Post): Post

    fun findById(id: BigInteger): Optional<Post>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<Post>
}