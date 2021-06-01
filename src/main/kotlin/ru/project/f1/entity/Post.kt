package ru.project.f1.entity

import java.math.BigInteger
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent

@Entity
@Table(name = "post")
data class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var title: String = "",
    @field:NotBlank var text: String = "s",
    @field:NotBlank var author: String,
    @field:PastOrPresent var createdDate: LocalDateTime
) {
    constructor(
        postTitle: String,
        postText: String,
        postAuthor: String,
        postCreatedDate: LocalDateTime
    ) : this(title = postTitle, text = postText, author = postAuthor, createdDate = postCreatedDate)

    constructor(postAuthor: String, postCreatedDate: LocalDateTime) : this(
        author = postAuthor,
        createdDate = postCreatedDate
    )
}
