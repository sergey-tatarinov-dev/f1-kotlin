package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var login: String,
    @field:NotBlank var password: String,
    @field:NotNull @Enumerated(EnumType.STRING) var role: Role
) {
    constructor(userLogin: String, userPassword: String) : this(login = userLogin, password = userPassword, role = Role.USER)
}
