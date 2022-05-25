package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "track")
data class Track(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var circuitName: String = "",
    @field:NotBlank var length: Int = 0,
    @field:NotBlank var lapCount: Int = 0,
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "country_id") var country: Country
)