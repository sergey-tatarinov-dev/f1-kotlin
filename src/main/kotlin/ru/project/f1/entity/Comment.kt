package ru.project.f1.entity

import java.math.BigInteger
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "comment")
data class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var text: String,
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "author_id") var author: User,
    @field:NotNull @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "news_id") var news: News,
    @field:NotNull var createdDate: LocalDateTime = LocalDateTime.now()
)
