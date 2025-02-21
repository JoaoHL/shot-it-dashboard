package br.com.acgj.shotit.presentation.authentication

import br.com.acgj.shotit.domain.BadRequestError
import br.com.acgj.shotit.domain.NotFoundError
import br.com.acgj.shotit.infra.UserRepository
import br.com.acgj.shotit.infra.jwt.JWTService
import br.com.acgj.shotit.infra.security.UserDetailsImpl
import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val manager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val producer: AuthenticationProducer,
    private val jwtService: JWTService
) {

    @Transactional
    fun register(request: SignUpRequest){
        val user = request.toUser(passwordEncoder)
        val created = userRepository.save(user)
        producer.sendCreateUserEvent(created)
    }

    fun authenticate(request: SignInRequest): String {
        val token = UsernamePasswordAuthenticationToken(request.email, request.password)
        try {
            val authentication = manager.authenticate(token)
            val user = authentication.principal as UserDetailsImpl

            return jwtService.generateToken(user.unwrap())
        }
        catch (exception: AuthenticationException){
            throw BadRequestError("Authentication Error", "authentication_error", "Invalid Credentials")
        }
    }

}