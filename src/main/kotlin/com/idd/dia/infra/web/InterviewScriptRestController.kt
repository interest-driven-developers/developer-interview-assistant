package com.idd.dia.infra.web

import com.idd.dia.application.dto.InterviewScriptRequest
import com.idd.dia.application.dto.InterviewScriptResponse
import com.idd.dia.application.service.InterviewScriptService
import com.idd.dia.infra.security.LoginUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v0/interview-scripts")
class InterviewScriptRestController(
    private val interviewScriptService: InterviewScriptService
) {

    @GetMapping
    fun getInterviewScripts(
        @LoginUser userPk: Long,
        @PageableDefault pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<InterviewScriptResponse>>> {

        return interviewScriptService.getScripts(
            requestUserPk = userPk,
            pageable = pageable
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
