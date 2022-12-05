package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import ru.project.f1.service.UserService
import ru.project.f1.utils.SecurityUtils.Companion.encoded
import ru.project.f1.utils.SecurityUtils.Companion.getUser
import ru.project.f1.utils.UiUtils.Companion.failBox
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar


@Route("change-password")
@Component
@PageTitle("F1 | Change password")
@PreserveOnRefresh
@UIScope
@Secured("USER", "MODERATOR", "ADMIN")
class ChangePasswordView : KComposite() {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userService: UserService
    private lateinit var oldPasswordField: PasswordField
    private lateinit var newPasswordField: PasswordField
    private lateinit var repeatPasswordField: PasswordField

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                formLayout {
                    alignSelf = FlexComponent.Alignment.CENTER
                    width = "17%"
                    h2("Change password")
                    oldPasswordField = passwordField("Old password") {
                        setWidthFull()
                        placeholder = "Enter old password"
                    }
                    newPasswordField = passwordField("New password") {
                        setWidthFull()
                        placeholder = "Enter new password"
                    }
                    repeatPasswordField = passwordField("Re-enter new password") {
                        setWidthFull()
                        placeholder = "Re-enter new password"
                    }
                    button("Change password") {
                        setWidthFull()
                        setPrimary()
                        onLeftClick { changePassword() }
                    }
                }
            }
        }
    }

    fun changePassword() {
        val user = getUser()
        val oldPassword = oldPasswordField.value
        val token = UsernamePasswordAuthenticationToken(user.login, oldPassword)
        if (authenticationManager.authenticate(token) != null) {
            val newPassword = newPasswordField.value
            val repeatPassword = repeatPasswordField.value
            if (newPassword == repeatPassword) {
                user.password = newPassword.encoded()
                userService.save(user)
                setLocation("/profile/${user.login}")
            } else {
                failBox("Password mismatch")
            }
        } else {
            failBox("Wrong old password")
        }
    }
}