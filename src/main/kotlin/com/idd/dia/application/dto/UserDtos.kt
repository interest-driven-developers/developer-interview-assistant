package com.idd.dia.application.dto

import com.idd.dia.application.domain.User

data class UserResponse(
    val pk: Long,
    val githubId: String
) {
    constructor(user: User) : this(
        pk = user.pk, githubId = user.githubId
    )
}

data class TokenResponse(
    val token: String
)
