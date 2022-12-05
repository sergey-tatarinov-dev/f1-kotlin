package ru.project.f1.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.project.f1.entity.News
import java.math.BigInteger

@Repository
interface NewsRepository : JpaRepository<News, BigInteger> {

    fun findAllBySuggestedAndDeleted(suggested: Boolean, deleted: Boolean, pageable: Pageable): Page<News>

    fun countAllBySuggested(suggested: Boolean): Int

    @Query("""
        update News
        set suggested = false 
        where id = :id
    """)
    @Modifying
    fun publish(id: BigInteger)

    @Query("""
        update News
        set suggested = false, deleted = true
        where id = :id
    """)
    @Modifying
    fun refuse(id: BigInteger)

    @Query("""
        update News
        set deleted = true 
        where id = :id
    """)
    @Modifying
    override fun deleteById(id: BigInteger)
}