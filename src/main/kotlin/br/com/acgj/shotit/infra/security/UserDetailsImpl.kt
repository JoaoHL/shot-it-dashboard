package br.com.acgj.shotit.infra.security

import br.com.acgj.shotit.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsImpl(val user: User) : UserDetails {
    fun unwrap() = user
    override fun getAuthorities() = mutableListOf<GrantedAuthority>()
    override fun getPassword() = user.password
    override fun getUsername() = user.email
}
