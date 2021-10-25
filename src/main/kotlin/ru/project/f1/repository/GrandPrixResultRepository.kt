package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.project.f1.entity.GrandPrixResult
import java.math.BigInteger

@Repository
interface GrandPrixResultRepository : JpaRepository<GrandPrixResult, BigInteger> {

    /*@Query("from GrandPrixResult order by grandPrix.id, position")
    override fun findAll(pageable: Pageable): Page<GrandPrixResult>*/

    @Query("select distinct grandPrix.id from GrandPrixResult order by grandPrix.id")
    fun findDistinctByGrandPrixId(): List<BigInteger>

}