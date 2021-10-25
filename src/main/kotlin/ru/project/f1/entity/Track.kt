package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "track")
data class Track(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var circuitName: String = "",
    @field:NotBlank var length: Int = 0,
    @field:NotBlank var lapCount: Int = 0,
)