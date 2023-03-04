package com.idd.dia.infra.web

import com.idd.dia.application.dto.InterviewScriptRequest
import com.idd.dia.application.dto.InterviewScriptResponse
import com.idd.dia.application.service.InterviewScriptService
import com.idd.dia.infra.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v0/interview-scripts")
class InterviewScriptRestController(
    private val interviewScriptService: InterviewScriptService
) {

    @PostMapping
    fun postInterviewScript(
        @LoginUser userPk: Long,
        @RequestParam questionPk: Long,
        @RequestBody interviewScriptRequest: InterviewScriptRequest
    ): ResponseEntity<ApiResponse<InterviewScriptResponse>> {

        return interviewScriptService.postScript(
            requestUserPk = userPk,
            questionPk = questionPk,
            interviewScriptRequest = interviewScriptRequest
        ).run {
            ResponseEntity.ok(
                ApiResponse.success(data = this)
            )
        }
    }

    @GetMapping
    fun getInterviewScript(
        @LoginUser userPk: Long,
        @RequestParam questionPk: Long,
    ): ResponseEntity<ApiResponse<InterviewScriptResponse>> {

        return interviewScriptService.getScript(
            requestUserPk = userPk,
            questionPk = questionPk
        ).run {
            ResponseEntity.ok(
                ApiResponse.success(data = this)
            )
        }
    }

    @PatchMapping("/{scriptPk}")
    fun updateInterviewScript(
        @PathVariable scriptPk: Long,
        @LoginUser userPk: Long,
        @RequestBody interviewScriptRequest: InterviewScriptRequest
    ): ResponseEntity<ApiResponse<InterviewScriptResponse>> {

        return interviewScriptService.updateScript(
            requestUserPk = userPk,
            scriptPk = scriptPk,
            interviewScriptRequest = interviewScriptRequest
        ).run {
            ResponseEntity.ok(
                ApiResponse.success(data = this)
            )
        }
    }
}
