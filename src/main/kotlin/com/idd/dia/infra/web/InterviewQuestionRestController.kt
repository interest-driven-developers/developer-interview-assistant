package com.idd.dia.infra.web

import com.idd.dia.application.dto.InterviewQuestionRequest
import com.idd.dia.application.dto.InterviewQuestionResponse
import com.idd.dia.application.service.InterviewQuestionService
import com.idd.dia.infra.security.LoginUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v0/interview-questions")
class InterviewQuestionRestController(
    private val interviewQuestionService: InterviewQuestionService
) {

    @GetMapping("/{questionPk}")
    fun getInterviewQuestion(
        @PathVariable questionPk: Long,
        @LoginUser userPk: Long?
    ): ResponseEntity<ApiResponse<InterviewQuestionResponse>> {
        return ResponseEntity.ok(
            ApiResponse.success(data = interviewQuestionService.get(questionPk, userPk))
        )
    }

    @GetMapping
    fun getInterviewQuestions(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<InterviewQuestionResponse>>> {
        return ResponseEntity.ok(
            ApiResponse.success(data = interviewQuestionService.get(pageable))
        )
    }

    @GetMapping("/my")
    fun getMyInterviewQuestions(
        @LoginUser userPk: Long,
        @PageableDefault pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<InterviewQuestionResponse>>> {
        return ResponseEntity.ok(
            ApiResponse.success(data = interviewQuestionService.getMy(userPk, pageable))
        )
    }

    @PostMapping
    fun postInterviewQuestion(
        @LoginUser userPk: Long,
        @RequestBody request: InterviewQuestionRequest
    ): ResponseEntity<ApiResponse<InterviewQuestionResponse>> {
        return ResponseEntity.ok(
            ApiResponse.success(data = interviewQuestionService.post(userPk, request))
        )
    }
}
