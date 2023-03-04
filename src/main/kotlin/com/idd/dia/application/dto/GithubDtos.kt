package com.idd.dia.application.dto

data class GithubAccessTokenRequest(
    val code: String,
    val clientId: String,
    val clientSecret: String,
)

data class GithubAccessTokenResponse(
    val accessToken: String,
    val tokenType: String,
    val scope: String
)

data class GithubUserData(
    val login: String,
    val id: Long,
    val nodeId: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val company: String?,
)
