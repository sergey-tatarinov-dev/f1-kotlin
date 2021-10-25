package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "team")
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var fullName: String = "",
    @field:NotBlank var name: String = "",
)