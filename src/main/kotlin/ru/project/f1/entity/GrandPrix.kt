package ru.project.f1.entity

import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "grand_prix")
data class GrandPrix(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    var date: LocalDate,
    @field:NotBlank var fullName: String = "",
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "track_id") var track: Track,
    var grandPrixOver: Boolean = false,
    var deleted: Boolean = false,
    var sprint: Boolean = false,
    var needApplyHalfPointsMultiplier: Boolean = false,
)