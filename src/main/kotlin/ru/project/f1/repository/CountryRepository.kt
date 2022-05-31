package ru.project.f1.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.project.f1.entity.Country
import java.math.BigInteger

@Repository
interface CountryRepository : JpaRepository<Country, BigInteger>