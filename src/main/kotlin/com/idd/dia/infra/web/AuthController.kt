package com.idd.dia.infra.web

import com.idd.dia.application.dto.TokenResponse
import com.idd.dia.application.service.GithubLoginService
import com.idd.dia.infra.security.JwtTokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/v0/auth")
class AuthController(
    private val jwtTokenProvider: JwtTokenProvider,
    private val githubLoginService: GithubLoginService
) {
    @GetMapping("/github/oauth/callback")
    fun githubOauthCallBack(
        @RequestParam @NotEmpty(message = "인증 코드가 비어있습니다.") code: String,
    ): ResponseEntity<ApiResponse<TokenResponse>> {
        val userDto = githubLoginService.login(code)
        return ResponseEntity.ok(
            ApiResponse.success(
                jwtTokenProvider.createToken(userResponse = userDto)
            )
        )
    }
}
