package ru.project.f1.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import ru.project.f1.entity.User
import java.math.BigInteger
import java.util.*

interface UserService {

    @Transactional
    fun save(user: User): User

    fun findById(id: BigInteger): Optional<User>

    @Transactional
    fun deleteById(id: BigInteger)

    fun findAll(pageable: Pageable): Page<User>

    fun findByLoginAndPassword(login: String, password: String): Optional<User>

    fun findByLogin(login: String): Optional<User>
}