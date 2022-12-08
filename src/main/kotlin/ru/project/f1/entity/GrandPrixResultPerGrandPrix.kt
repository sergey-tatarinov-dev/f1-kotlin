package ru.project.f1.entity

import java.math.BigInteger

data class GrandPrixResultPerGrandPrix(
    val driverId: BigInteger,
    val position: Int,
    val driverName: String,
    val countryId: BigInteger,
    var teamName: String = "",
    var logoId: BigInteger = BigInteger.ZERO
) {
    constructor(driverId: BigInteger,
                position: Int,
                driverName: String,
                countryId: BigInteger) : this(driverId, position, driverName, countryId, "", BigInteger.ZERO)
}
