package com.idd.dia.infra.security

import com.idd.dia.application.domain.User
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import java.util.Date

/**
 * @see JwtTokenProvider
 */
class JwtTokenProviderTest : ExpectSpec({

    val jwtTokenProvider = JwtTokenProvider()

    expect("유저 엔티티와 생성 시간으로 토큰을 생성할 수 있다.") {
        val user = User(githubId = "jaeykweon", pk = 1L)
        val createDate = Date()

        shouldNotThrowAny { jwtTokenProvider.createToken(user, createDate) }
    }

    expect("토큰 만료 시간이 지나면 유효하지 않다.") {
        val user = User(githubId = "jaeykweon", pk = 1L)
        val now = Date()

        val expiredDate = Date(now.time - (ONE_DAY_IN_MILLISECONDS * 7))
        val expiredToken = jwtTokenProvider.createToken(user, expiredDate).token
        val validDate = Date(now.time + 60000 - (ONE_DAY_IN_MILLISECONDS * 7))
        val validToken = jwtTokenProvider.createToken(user, validDate).token

        jwtTokenProvider.isValid(expiredToken) shouldBe false
        jwtTokenProvider.isValid(validToken) shouldBe true
    }

    expect("토큰에서 userPk를 추출할 수 있다") {
        val user = User(githubId = "jaeykweon", pk = 1L)
        val createDate = Date()

        val createdToken = jwtTokenProvider.createToken(user, createDate).token

        jwtTokenProvider.isValid(createdToken) shouldBe true
        jwtTokenProvider.getUserPkFrom(createdToken) shouldBe 1L
    }
})
