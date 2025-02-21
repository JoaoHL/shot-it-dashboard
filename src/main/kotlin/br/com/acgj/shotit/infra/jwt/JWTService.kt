package br.com.acgj.shotit.infra.jwt

import br.com.acgj.shotit.domain.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JWTService(@Value("\${jwt.secret}") secret: String) {

    private final val algorithm: Algorithm = Algorithm.HMAC256(secret)

    fun generateToken(user: User): String = JWT.create().withIssuer("ClipIt").withClaim("email", user.email).sign(algorithm)

    fun extractUserFromToken(token: String): String? {
        try {
            val jwt = JWT.require(algorithm).build().verify(token)
            return jwt.getClaim("email").asString()
        }
        catch (exception: JWTVerificationException){
            return null
        }
    }


}