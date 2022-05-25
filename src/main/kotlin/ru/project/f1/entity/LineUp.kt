package ru.project.f1.entity

import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "line_up")
@Embeddable
data class LineUp(
    @EmbeddedId
    var lineUpId: LineUpId,
    var year: Int = 0,
)