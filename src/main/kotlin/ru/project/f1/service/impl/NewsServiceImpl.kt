package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.News
import ru.project.f1.repository.NewsRepository
import ru.project.f1.service.NewsService
import java.math.BigInteger
import java.util.*

@Service
class NewsServiceImpl : NewsService {

    @Autowired
    private lateinit var newsRepository: NewsRepository

    override fun save(news: News): News = newsRepository.save(news)

    override fun findById(id: BigInteger): Optional<News> = newsRepository.findById(id)

    override fun deleteById(id: BigInteger): Unit = newsRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<News> = newsRepository.findAll(pageable)

    override fun findAllBySuggestedAndDeleted(suggested: Boolean, deleted: Boolean, pageable: Pageable): Page<News> =
        newsRepository.findAllBySuggestedAndDeleted(suggested, deleted, pageable)

    override fun countAllBySuggested(suggested: Boolean): Int = newsRepository.countAllBySuggested(suggested)

    override fun publish(id: BigInteger) = newsRepository.publish(id)

    override fun refuse(id: BigInteger) = newsRepository.refuse(id)
}