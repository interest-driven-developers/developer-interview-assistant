package com.idd.dia.application.service

import com.idd.dia.application.domain.User
import com.idd.dia.application.domain.UserRepository
import com.idd.dia.application.dto.GithubAccessTokenRequest
import com.idd.dia.application.dto.GithubAccessTokenResponse
import com.idd.dia.application.dto.GithubUserData
import com.idd.dia.application.dto.UserResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GithubLoginService(
    private val restTemplate: RestTemplate,
    private val userRepository: UserRepository,

    @Value("\${oauth.github.access_url}")
    private val githubAccessTokenUrl: String = "",
    @Value("\${oauth.github.user_data_url}")
    private val githubUserDataUrl: String = "",
    @Value("\${oauth.github.client_id}")
    private val githubClientId: String = "",
    @Value("\${oauth.github.client_secret}")
    private val githubClientSecret: String = ""
) {

    fun login(code: String): UserResponse {
        val header = createHeader()
        val githubAccessTokenGetRequest = GithubAccessTokenRequest(
            code = code,
            clientId = githubClientId,
            clientSecret = githubClientSecret
        )

        val githubAccessToken = restTemplate.getGithubUserAccessToken(githubAccessTokenGetRequest, header)

        header.set("Authorization", "Bearer $githubAccessToken")
        val githubUserData = restTemplate.getGithubUserData(header)
        val githubId = githubUserData.login

        val user = userRepository.findByGithubId(githubId)
            ?: userRepository.save(User(githubId = githubId))

        return UserResponse(user)
    }

    private fun createHeader(
        contentType: MediaType = MediaType.APPLICATION_JSON,
        accept: List<MediaType> = listOf(MediaType.APPLICATION_JSON),
    ) = HttpHeaders().apply {
        this.contentType = contentType
        this.accept = accept
    }

    private fun RestTemplate.getGithubUserAccessToken(request: Any, header: HttpHeaders): String {
        return this.postForEntity(
            githubAccessTokenUrl,
            HttpEntity(request, header),
            GithubAccessTokenResponse::class.java
        ).body?.accessToken ?: throw IllegalStateException("CANNOT GET GITHUB ACCESS TOKEN")
    }

    private fun RestTemplate.getGithubUserData(header: HttpHeaders): GithubUserData {
        return this.exchange(
            githubUserDataUrl,
            HttpMethod.GET,
            HttpEntity<Map<String, Any>>(header),
            GithubUserData::class.java
        ).body ?: throw IllegalStateException("CANNOT GET GITHUB USER DATA")
    }
}
