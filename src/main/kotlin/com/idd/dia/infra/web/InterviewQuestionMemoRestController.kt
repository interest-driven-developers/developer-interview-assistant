package com.idd.dia.infra.web

import com.idd.dia.application.dto.InterviewQuestionMemoRequest
import com.idd.dia.application.dto.InterviewQuestionMemoResponse
import com.idd.dia.application.dto.InterviewQuestionMemosResponse
import com.idd.dia.application.service.InterviewQuestionMemoService
import com.idd.dia.infra.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v0/memos")
class InterviewQuestionMemoRestController(
    private val interviewQuestionMemoService: InterviewQuestionMemoService
) {

    @GetMapping
    fun getMemos(
        @LoginUser userPk: Long,
        @RequestParam questionPk: Long
    ): ResponseEntity<ApiResponse<InterviewQuestionMemosResponse>> {
        return ResponseEntity.ok(
            ApiResponse.success(
                data = interviewQuestionMemoService.getAll(userPk = userPk, questionPk = questionPk)
            )
        )
    }

    @PostMapping
    fun postMemo(
        @LoginUser userPk: Long,
        @RequestParam questionPk: Long,
        @RequestBody request: InterviewQuestionMemoRequest
    ): ResponseEntity<ApiResponse<InterviewQuestionMemoResponse>> {
        return ResponseEntity.ok(
            ApiResponse.success(
                data = interviewQuestionMemoService.post(userPk = userPk, questionPk = questionPk, request = request)
            )
        )
    }

    @PatchMapping("/{memoPk}")
    fun updateMemo(
        @PathVariable memoPk: Long,
        @LoginUser userPk: Long,
        @RequestBody request: InterviewQuestionMemoRequest
    ): ResponseEntity<ApiResponse<InterviewQuestionMemoResponse>> {
        return ResponseEntity.ok(
            ApiResponse.success(
                data = interviewQuestionMemoService.update(
                    userPk = userPk, memoPk = memoPk, newContent = request.content
                )
            )
        )
    }

    @DeleteMapping("/{memoPk}")
    fun deleteMemo(
        @PathVariable memoPk: Long,
        @LoginUser userPk: Long,
    ) {
        return interviewQuestionMemoService.delete(userPk = userPk, memoPk = memoPk).run {
            ResponseEntity.ok(
                ApiResponse.success(data = null, message = "delete success")
            )
        }
    }
}
