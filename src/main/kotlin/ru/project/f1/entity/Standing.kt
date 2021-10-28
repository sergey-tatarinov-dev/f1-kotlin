package ru.project.f1.entity

import org.hibernate.annotations.Type
import javax.persistence.*

@MappedSuperclass
abstract class Standing(
    @Id
    open val id: Int = 0,
    open val name: String = "",
    open val sum: Double = .0,
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    open val points: List<Double> = emptyList()
)