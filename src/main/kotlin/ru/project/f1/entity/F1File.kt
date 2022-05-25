package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "file")
data class F1File(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var name: String = "",
    @field:NotBlank var fullPath: String = "",
    @field:NotBlank var extension: String = ""
)