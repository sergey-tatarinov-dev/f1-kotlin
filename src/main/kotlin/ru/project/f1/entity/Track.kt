package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "track")
data class Track(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var circuitName: String = "",
    @field:Min(2500) @Max(7500) var length: Int = 0,
    @field:Min(35) @Max(100) var lapCount: Int = 0,
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "country_id") var country: Country
)