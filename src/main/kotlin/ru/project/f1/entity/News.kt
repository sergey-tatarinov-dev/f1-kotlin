package ru.project.f1.entity

import java.math.BigInteger
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent

@Entity
@Table(name = "news")
data class News(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var title: String = "",
    @field:NotBlank var text: String = "s",
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "author_id") var author: User,
    @field:PastOrPresent var createdDate: LocalDateTime,
    var suggested: Boolean = false,
    var deleted: Boolean = false
) {
    constructor(newsAuthor: User, newsCreatedDate: LocalDateTime) : this(
        author = newsAuthor,
        createdDate = newsCreatedDate
    )
}
