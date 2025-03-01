package br.com.acgj.shotit.core.infra.auth

import br.com.acgj.shotit.core.domain.UnauthorizedError
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class ApplicationUser {
    fun fromContext() = SecurityContextHolder.getContext().authentication
        ?.takeIf { it is UsernamePasswordAuthenticationToken && it.principal is UserDetailsImpl }
        ?.let { (it.principal as UserDetailsImpl).user }

    fun fromAuthorizedContext() = fromContext() ?: throw UnauthorizedError()
}
