package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "driver")
data class Driver(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var name: String = "",
    @field:NotBlank var surname: String = "",
    @field:NotBlank var raceNumber: Int = 0,
)