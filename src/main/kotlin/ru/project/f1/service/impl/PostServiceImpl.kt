package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.Post
import ru.project.f1.repository.PostRepository
import ru.project.f1.repository.UserRepository
import ru.project.f1.service.PostService
import java.math.BigInteger
import java.util.*

@Service
class PostServiceImpl : PostService {

    @Autowired
    private lateinit var postRepository: PostRepository

    override fun save(post: Post): Post = postRepository.save(post)

    override fun findById(id: BigInteger): Optional<Post> = postRepository.findById(id)

    override fun deleteById(id: BigInteger): Unit = postRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<Post> = postRepository.findAll(pageable)
}