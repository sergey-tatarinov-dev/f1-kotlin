package ru.project.f1.entity

import java.math.BigInteger

data class GrandPrixResultPerGrandPrix(
    val driverId: BigInteger,
    val position: Int,
    val driverName: String,
    val countryId: BigInteger,
    val teamName: String,
    val logoId: BigInteger
)
