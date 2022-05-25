package ru.project.f1.entity

import java.math.BigInteger

data class GrandPrixResultPerDriver(
    val grandPrixId: BigInteger,
    val grandPrixName: String,
    val points: Double
)
