package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.project.f1.entity.GrandPrix
import ru.project.f1.entity.GrandPrixResult
import java.math.BigInteger

@Repository
interface GrandPrixRepository : JpaRepository<GrandPrix, BigInteger> {

    /*@Query("from GrandPrixResult order by grandPrix.id, position")
    override fun findAll(pageable: Pageable): Page<GrandPrixResult>*/

    @Query("from GrandPrix " +
            "where id in (select grandPrix.id from GrandPrixResult)" +
            "group by fullName, id " +
            "order by id")
    fun findAllGrandPrix(): List<GrandPrix>

}