package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.project.f1.entity.GrandPrix
import java.math.BigInteger

@Repository
interface GrandPrixRepository : JpaRepository<GrandPrix, BigInteger> {

    @Query("""
        from GrandPrix 
        where extract(year from date) = :year 
        group by fullName, id 
        order by date
    """)
    fun findAllGrandPrixByYear(year: Int): List<GrandPrix>

    @Query("""
        select distinct extract(year from date) as year 
        from grand_prix 
        order by year
    """, nativeQuery = true)
    fun findAllYears(): List<Int>

}