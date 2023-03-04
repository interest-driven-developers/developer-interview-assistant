package com.idd.dia.infra.security

import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginUserResolver(
    private val jwtTokenProvider: JwtTokenProvider,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Long? {
        val authorization = webRequest.getHeader(AUTHORIZATION) ?: return null

        val token = extractBearerToken(authorization)
        if (!jwtTokenProvider.isValid(token)) {
            throw LoginFailedException("올바르지 않은 토큰 정보입니다.")
        }

        return jwtTokenProvider.getUserPkFrom(token)
    }

    private fun extractBearerToken(authorization: String): String {
        val (tokenType, token) = splitToTokenFormat(authorization)
        if (tokenType != BEARER) {
            throw LoginFailedException()
        }
        return token
    }

    private fun splitToTokenFormat(authorization: String): Pair<String, String> {
        return try {
            val tokenFormat = authorization.split(" ")
            tokenFormat[0] to tokenFormat[1]
        } catch (e: IndexOutOfBoundsException) {
            throw LoginFailedException()
        }
    }

    companion object {
        private const val BEARER = "Bearer"
    }
}
