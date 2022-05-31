package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.Country
import ru.project.f1.repository.CountryRepository
import ru.project.f1.service.CountryService
import java.math.BigInteger
import java.util.*

@Service
class CountryServiceImpl : CountryService {

    @Autowired
    private lateinit var countryRepository: CountryRepository

    override fun save(country: Country): Country = countryRepository.save(country)

    override fun findById(id: BigInteger): Optional<Country> = countryRepository.findById(id)

    override fun deleteById(id: BigInteger) = countryRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<Country> = countryRepository.findAll(pageable)
}