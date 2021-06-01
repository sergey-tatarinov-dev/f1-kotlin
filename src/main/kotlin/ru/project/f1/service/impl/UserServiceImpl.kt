package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.User
import ru.project.f1.repository.UserRepository
import ru.project.f1.service.UserService
import java.math.BigInteger
import java.util.*

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun save(user: User): User = userRepository.save(user)

    override fun findById(id: BigInteger) = userRepository.findById(id)

    override fun deleteById(id: BigInteger) = userRepository.deleteById(id)

    override fun findAll(pageable: Pageable) = userRepository.findAll(pageable)

    override fun findByLoginAndPassword(login: String, password: String) = userRepository.findByLoginAndPassword(login, password)

    override fun findByLogin(login: String): Optional<User> = userRepository.findByLogin(login)
}