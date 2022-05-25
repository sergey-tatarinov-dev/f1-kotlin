package ru.project.f1.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.project.f1.security.CustomRequestCache
import ru.project.f1.service.UserService
import ru.project.f1.utils.SecurityUtils

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userService: UserService

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? = super.authenticationManagerBean()

    override fun configure(http: HttpSecurity) {
        http {
            httpBasic {}
            requestCache { requestCache() }
            authorizeRequests {
                authorize("/admin", hasRole("ADMIN"))
                authorize(SecurityUtils::isFrameworkInternalRequest, permitAll)
                authorize("/profile/**", fullyAuthenticated)
                authorize("/logout", fullyAuthenticated)
                authorize("/**", permitAll)
            }
            formLogin {
                loginPage = "/login"
            }
            logout {
                logoutUrl = "/logout"
                invalidateHttpSession = true
                clearAuthentication = true
                deleteCookies("JSESSIONID")
                logoutSuccessUrl = "/news"
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
                "/icons/**",
                "/styles/**",
                "/images/**",
                "/frontend-es5/**", "/frontend-es6/**"
            )
    }

    @Bean
    fun requestCache(): CustomRequestCache? {
        return CustomRequestCache()
    }

    @Bean
    protected fun passwordEncoder(): PasswordEncoder? = BCryptPasswordEncoder(12)

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider = DaoAuthenticationProvider().apply {
        setPasswordEncoder(passwordEncoder())
        setUserDetailsService(userService)
    }
}