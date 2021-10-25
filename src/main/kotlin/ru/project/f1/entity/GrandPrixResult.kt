package ru.project.f1.entity

import javax.persistence.*

@Entity
@Table(name = "grand_prix_result")
data class GrandPrixResult(

    @EmbeddedId
    var grandPrixResultId: GrandPrixResultId,

    @ManyToOne
    @MapsId("grandPrixId")
    @JoinColumn(name = "grand_prix_id")
    var grandPrix: GrandPrix,

    @ManyToOne
    @MapsId("driverId")
    @JoinColumn(name = "driver_id")
    var driver: Driver,

    var position: Int = 0,
    var setFastestLap: Boolean = false,
    var points: Double = 0.0,
)

