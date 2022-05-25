package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "driver")
data class Driver(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var name: String = "",
    @field:NotBlank var surname: String = "",
    @field:NotBlank var raceNumber: Int = 0,
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "country_id") var country: Country
)