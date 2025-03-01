package br.com.acgj.shotit.core.infra.auth

import br.com.acgj.shotit.core.domain.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val repository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetailsImpl = repository.findByEmail(email)
        .map { UserDetailsImpl(it) }
        .orElseThrow { UsernameNotFoundException("Failed to find user") }
}