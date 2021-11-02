package ru.project.f1.entity

import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "team")
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var fullName: String = "",
    @field:NotBlank var name: String = "",
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "country_id") var country: Country,
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "logo_id") var file: F1File
)