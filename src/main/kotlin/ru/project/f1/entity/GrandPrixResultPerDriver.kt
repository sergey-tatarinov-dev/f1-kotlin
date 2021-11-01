package ru.project.f1.entity

import java.math.BigInteger

data class GrandPrixResultPerDriver(
    val driverId: BigInteger,
    val driverName: String,
    val teamName: String,
    val position: Int,
)
