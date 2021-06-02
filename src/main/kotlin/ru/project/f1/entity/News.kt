package ru.project.f1.entity

import java.math.BigInteger
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent

@Entity
@Table(name = "post")
data class News(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var title: String = "",
    @field:NotBlank var text: String = "s",
    @field:NotBlank var author: String,
    @field:PastOrPresent var createdDate: LocalDateTime
) {
    constructor(
        newsTitle: String,
        newsText: String,
        newsAuthor: String,
        newsCreatedDate: LocalDateTime
    ) : this(title = newsTitle, text = newsText, author = newsAuthor, createdDate = newsCreatedDate)

    constructor(newsAuthor: String, newsCreatedDate: LocalDateTime) : this(
        author = newsAuthor,
        createdDate = newsCreatedDate
    )
}
