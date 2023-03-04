package com.idd.dia.infra.security

import com.idd.dia.application.domain.User
import com.idd.dia.application.dto.TokenResponse
import com.idd.dia.application.dto.UserResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import javax.annotation.PostConstruct
import javax.crypto.SecretKey

const val ONE_DAY_IN_MILLISECONDS: Long = 1000 * 60 * 60 * 24
const val USER_PK = "userPk"

@Component
class JwtTokenProvider(
    private val signingKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256),
    private val expirationInMilliseconds: Long = ONE_DAY_IN_MILLISECONDS * 7
) {
    @PostConstruct
    fun debug() {
        println(createToken(User(githubId = "jaeykweon", pk = 1)))
    }

    fun createToken(userResponse: UserResponse, createAt: Date = Date()): TokenResponse {
        return createToken(userPk = userResponse.pk, createAt = createAt).run(::TokenResponse)
    }

    fun createToken(user: User, createAt: Date = Date()): TokenResponse {
        return createToken(userPk = user.pk, createAt = createAt).run(::TokenResponse)
    }

    private fun createToken(userPk: Long ,createAt: Date): String {
        val claims: Claims = Jwts.claims(mapOf(USER_PK to userPk))
        val expiration = Date(createAt.time + expirationInMilliseconds)
        return Jwts.builder()
            .addClaims(claims)
            .setIssuedAt(createAt)
            .setExpiration(expiration)
            .signWith(signingKey)
            .compact()
    }

    fun getUserPkFrom(token: String): Long? {
        return getClaimsJws(token)
            .body[USER_PK]
            .toString().toLong()
    }

    fun isValid(token: String): Boolean {
        return try {
            getClaimsJws(token)
            true
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun getClaimsJws(token: String) = Jwts.parserBuilder()
        .setSigningKey(signingKey.encoded)
        .build()
        .parseClaimsJws(token)
}
