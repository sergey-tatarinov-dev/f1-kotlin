package ru.project.f1.entity

import com.vladmihalcea.hibernate.type.array.IntArrayType
import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "team_standings_2021")
@TypeDefs(
    TypeDef(name = "string-array", typeClass = StringArrayType::class),
        TypeDef(name = "int-array", typeClass = IntArrayType::class),
            TypeDef(name = "json", typeClass = JsonType::class)
)
data class TeamStanding(
    @Id
    val id: Int,
    val name: String,
    val sum: Double,
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    val points: List<Double>,
)