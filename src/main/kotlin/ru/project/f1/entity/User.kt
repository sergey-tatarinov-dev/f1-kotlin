package ru.project.f1.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.math.BigInteger
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: BigInteger = BigInteger.ZERO,
    @field:NotBlank var login: String,
    @field:NotBlank private var password: String,
    @field:NotNull @Enumerated(EnumType.STRING) var role: Role,
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "file_id") var userPic: F1File? = null
) : UserDetails {
    constructor(userLogin: String, userPassword: String) : this(
        login = userLogin,
        password = userPassword,
        role = Role.USER
    )

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(role)

    override fun getPassword(): String = password

    fun setPassword(password: String) {
        this.password = password
    }

    override fun getUsername(): String = login

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
