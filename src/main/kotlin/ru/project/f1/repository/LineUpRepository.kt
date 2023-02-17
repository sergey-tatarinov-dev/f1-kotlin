package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.project.f1.entity.Driver
import ru.project.f1.entity.LineUp
import java.math.BigInteger

@Repository
interface LineUpRepository : JpaRepository<LineUp, BigInteger> {
    fun findAllByYear(year: Int): List<LineUp>
}