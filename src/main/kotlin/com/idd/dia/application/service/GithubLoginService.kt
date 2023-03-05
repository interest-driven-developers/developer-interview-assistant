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
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GithubLoginService(
    @Value("\${oauth.github.access_url}")
    private val githubAccessTokenUrl: String,
    @Value("\${oauth.github.user_data_url}")
    private val githubUserDataUrl: String,
    @Value("\${oauth.github.client_id}")
    private val githubClientId: String,
    @Value("\${oauth.github.client_secret}")
    private val githubClientSecret: String,
    private val userRepository: UserRepository
) {

    fun login(code: String): UserResponse {
        val restTemplate = createRestTemplate()
        val header = createHeader()
        val githubAccessTokenGetRequest = GithubAccessTokenRequest(
            code = code,
            clientId = githubClientId,
            clientSecret = githubClientSecret
        )

        val githubAccessTokenResponse: ResponseEntity<GithubAccessTokenResponse> =
            restTemplate.postForEntity(
                githubAccessTokenUrl,
                HttpEntity(githubAccessTokenGetRequest, header),
                GithubAccessTokenResponse::class.java
            )

        header.set("Authorization", "Bearer " + githubAccessTokenResponse.body!!.accessToken)
        val githubUserDataResponse: ResponseEntity<GithubUserData> = restTemplate.exchange(
            githubUserDataUrl,
            HttpMethod.GET,
            HttpEntity<Map<String, Any>>(header),
            GithubUserData::class.java
        )

        val githubUserData = githubUserDataResponse.body!!
        val githubId = githubUserData.login

        val user = userRepository.findByGithubId(githubId)
            ?: userRepository.save(User(githubId = githubId))

        return UserResponse(user)
    }

    private fun createRestTemplate(connectTimeOut: Int = 5000, readTimeOut: Int = 5000): RestTemplate {
        val httpRequestFactory = HttpComponentsClientHttpRequestFactory().apply {
            setConnectTimeout(connectTimeOut)
            setReadTimeout(readTimeOut)
        }
        return RestTemplate(httpRequestFactory)
    }

    private fun createHeader(
        contentType: MediaType = MediaType.APPLICATION_JSON,
        accept: List<MediaType> = listOf(MediaType.APPLICATION_JSON),
    ) = HttpHeaders().apply {
        this.contentType = contentType
        this.accept = accept
    }

}
