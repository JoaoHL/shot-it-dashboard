package br.com.acgj.shotit.core.infra.auth

import br.com.acgj.shotit.core.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsImpl(val user: User) : UserDetails {
    override fun getAuthorities() = mutableListOf<GrantedAuthority>()
    override fun getPassword() = user.password
    override fun getUsername() = user.email
}
