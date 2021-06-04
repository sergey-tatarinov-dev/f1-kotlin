package ru.project.f1.entity

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ADMIN,
    MODERATOR,
    USER;

    override fun getAuthority(): String {
        return this.name
    }
}