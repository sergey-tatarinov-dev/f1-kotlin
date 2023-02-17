package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.project.f1.entity.Driver
import java.math.BigInteger

@Repository
interface DriverRepository : JpaRepository<Driver, BigInteger> {
    @Query("""
        select distinct extract(year from date) as year 
        from grand_prix 
        order by year
    """, nativeQuery = true)
    fun findAllGrandPrixYears(): List<Int>

    @Query("""
        select NEW ru.project.f1.entity.Driver(d.id, d.name, d.surname, d.raceNumber, d.country)
        from LineUp l inner join Driver d ON l.lineUpId.driverId = d.id 
        where l.year = :year
        order by l.lineUpId.teamId
    """)
    fun findAllDriversByYear(year: Int): List<Driver>

    @Query("""
        select distinct year 
        from LineUp
        order by year
    """)
    fun findAllDriverYears(): List<Int>

    fun findDriverByNameAndSurname(name: String, surname: String): Driver
}