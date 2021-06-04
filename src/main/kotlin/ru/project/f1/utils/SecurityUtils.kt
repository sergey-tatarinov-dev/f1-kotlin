package ru.project.f1.utils

import com.vaadin.flow.server.HandlerHelper
import com.vaadin.flow.shared.ApplicationConstants
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.security.access.annotation.Secured
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import ru.project.f1.entity.User
import javax.servlet.http.HttpServletRequest


class SecurityUtils {

    companion object {

        fun isFrameworkInternalRequest(request: HttpServletRequest): Boolean {
            val parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER)
            return (parameterValue != null
                    && HandlerHelper.RequestType.values().any { it.identifier.equals(parameterValue) })
        }

        fun isUserLoggedIn(): Boolean {
            val authentication = getAuthentication()
            return authentication !is AnonymousAuthenticationToken && authentication.isAuthenticated
        }

        fun getUser(): User {
            val authentication = getAuthentication()
            return (authentication.principal as User)
        }

        private fun getAuthentication(): Authentication {
            return SecurityContextHolder.getContext().authentication
        }

        fun isAccessGranted(securedClass: Class<*>?): Boolean {
            // Allow if no roles are required.
            val secured: Secured = AnnotationUtils.findAnnotation(securedClass!!, Secured::class.java) ?: return true

            // lookup needed role in user roles
            val allowedRoles: Array<String> = secured.value
            val userAuthentication: Authentication = SecurityContextHolder.getContext().authentication
            return userAuthentication.authorities.stream() //
                .map { obj: GrantedAuthority -> obj.authority }
                .anyMatch(allowedRoles::contains)
        }
    }

}