package br.com.acgj.shotit.presentation.authentication

import br.com.acgj.shotit.domain.User
import org.springframework.security.crypto.password.PasswordEncoder

data class SignInRequest(val email: String, val password: String)

data class SignUpRequest(val name: String, val username: String, val email: String, val password: String){
    fun toUser(encoder: PasswordEncoder) = User(
        username = username,
        name = name,
        email = email,
        password = encoder.encode(password)
    )
}