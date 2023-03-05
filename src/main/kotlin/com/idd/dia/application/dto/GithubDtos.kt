package com.idd.dia.application.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GithubAccessTokenRequest(
    val code: String,
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String,
)

data class GithubAccessTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
    val scope: String
)

data class GithubUserData(
    val login: String,
    val id: Long,
    @JsonProperty("node_id")
    val nodeId: String,
    @JsonProperty("avatar_url")
    val avatarUrl: String,
    @JsonProperty("html_url")
    val htmlUrl: String,
    val company: String?,
)
