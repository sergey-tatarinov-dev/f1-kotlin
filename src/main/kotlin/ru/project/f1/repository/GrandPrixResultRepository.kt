package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.project.f1.entity.GrandPrixResult
import ru.project.f1.entity.GrandPrixResultPerDriver
import ru.project.f1.entity.GrandPrixResultPerGrandPrix
import java.math.BigInteger

@Repository
interface GrandPrixResultRepository : JpaRepository<GrandPrixResult, BigInteger> {

    @Query("select distinct grandPrix.id from GrandPrixResult order by grandPrix.id")
    fun findDistinctByGrandPrixId(): List<BigInteger>

    @Query("select NEW ru.project.f1.entity.GrandPrixResultPerGrandPrix(" +
            "d.id, concat(d.name, ' ', d.surname), t.name, gpr.position" +
            ") " +
            "from GrandPrixResult gpr " +
            "   inner join Driver d ON gpr.driver.id = d.id " +
            "   inner join GrandPrix gp on gpr.grandPrix.id = gp.id " +
            "   inner join LineUp lu on lu.lineUpId.driverId = d.id " +
            "   inner join Team t on t.id = lu.lineUpId.teamId " +
            "where gpr.grandPrixResultId.grandPrixId = :id " +
            "order by gpr.position")
    fun findAllByGrandPrixId(id: Int): List<GrandPrixResultPerGrandPrix>

    @Query("select NEW ru.project.f1.entity.GrandPrixResultPerDriver(gp.id, gp.fullName, gpr.points) " +
            "from GrandPrixResult gpr " +
            "inner join GrandPrix gp on gp.id = gpr.grandPrixResultId.grandPrixId " +
            "where gpr.grandPrixResultId.driverId = :id " +
            "order by gpr.grandPrixResultId.driverId")
    fun findAllByDriverId(id: Int): List<GrandPrixResultPerDriver>

}