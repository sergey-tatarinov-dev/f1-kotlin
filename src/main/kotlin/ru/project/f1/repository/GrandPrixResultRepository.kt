package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.project.f1.entity.GrandPrixResult
import ru.project.f1.entity.GrandPrixResultPerDriver
import ru.project.f1.entity.GrandPrixResultPerGrandPrix
import ru.project.f1.entity.Team
import java.math.BigInteger

@Repository
interface GrandPrixResultRepository : JpaRepository<GrandPrixResult, BigInteger> {

    @Query("""
        select distinct grandPrix.id 
        from GrandPrixResult 
        order by grandPrix.id
    """)
    fun findDistinctByGrandPrixId(): List<BigInteger>

    @Query("""
        select NEW ru.project.f1.entity.GrandPrixResultPerGrandPrix(
        d.id, gpr.position, concat(d.name, ' ', d.surname), d.country.id
        )
        from GrandPrixResult gpr
            inner join Driver d ON gpr.driver.id = d.id
            inner join GrandPrix gp on gpr.grandPrix.id = gp.id
        where gpr.grandPrixResultId.grandPrixId = :id
        order by gpr.position
    """)
    fun findAllGrandPrixResultByIdAndYear(id: Int): List<GrandPrixResultPerGrandPrix>

    @Query("""
        select NEW ru.project.f1.entity.GrandPrixResultPerDriver(gp.id, gp.fullName, gpr.points) 
        from GrandPrixResult gpr
        inner join GrandPrix gp on gp.id = gpr.grandPrixResultId.grandPrixId 
        where gpr.grandPrixResultId.driverId = :id
        order by gpr.grandPrixResultId.driverId
    """)
    fun findAllByDriverId(id: Int): List<GrandPrixResultPerDriver>

    @Query("""
        select NEW ru.project.f1.entity.Team(t.id, t.fullName, t.name, t.country, t.file)
        from Team t
        inner join LineUp lu on t.id = lu.lineUpId.teamId
        inner join Driver d on d.id = lu.lineUpId.driverId
        where d.id = :id
    """)
    fun findTeamByDriverId(id: BigInteger): List<Team>

}