package ru.project.f1.entity

import java.math.BigInteger

data class GrandPrixResultPerGrandPrix(
    val driverId: BigInteger,
    val driverName: String,
    val teamName: String,
    val position: Int
)
