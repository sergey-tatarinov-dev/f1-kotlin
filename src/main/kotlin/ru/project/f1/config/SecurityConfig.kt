package ru.project.f1.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http {
            httpBasic {}
            authorizeRequests {
                authorize("/login/**", hasAuthority("ROLE_ADMIN"))
                authorize("/**", permitAll)
            }
            csrf { disable() }
        }
    }

    override fun configure(web: WebSecurity) {
        web
            .ignoring().antMatchers(
                "/VAADIN/**",
                "/frontend/**",
                "/webjars/**",
                "/images/**",
                "/frontend-es5/**", "/frontend-es6/**"
            )
    }
}