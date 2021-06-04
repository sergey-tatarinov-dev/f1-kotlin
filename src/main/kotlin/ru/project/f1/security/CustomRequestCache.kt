package ru.project.f1.security

import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.server.VaadinServletResponse
import org.springframework.security.web.savedrequest.DefaultSavedRequest
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.SavedRequest
import ru.project.f1.utils.SecurityUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomRequestCache : HttpSessionRequestCache() {

    fun resolveRedirectUrl(): String {
        val savedRequest: SavedRequest = getRequest(
            VaadinServletRequest.getCurrent().httpServletRequest,
            VaadinServletResponse.getCurrent().httpServletResponse
        )
        if (savedRequest is DefaultSavedRequest) {
            val requestURI = savedRequest.requestURI
            if (requestURI != null && requestURI.isNotEmpty() && !requestURI.contains("login")) { //
                return if (requestURI.startsWith("/")) requestURI.substring(1) else requestURI //
            }
        }
        return ""
    }

    override fun saveRequest(request: HttpServletRequest?, response: HttpServletResponse?) {
        if (!SecurityUtils.isFrameworkInternalRequest(request!!)) {
            super.saveRequest(request, response)
        }
    }

}
