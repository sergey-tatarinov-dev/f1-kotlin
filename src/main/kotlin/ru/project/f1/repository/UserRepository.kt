package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.project.f1.entity.User
import java.math.BigInteger
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, BigInteger> {
    fun findByLoginAndPassword(login: String, password: String): Optional<User>
    fun findByLogin(login: String): Optional<User>
}