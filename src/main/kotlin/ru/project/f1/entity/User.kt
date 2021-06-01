package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var login: String,
    @field:NotBlank var password: String
) {
    constructor(userLogin: String, userPassword: String): this(login = userLogin, password = userPassword)
}
