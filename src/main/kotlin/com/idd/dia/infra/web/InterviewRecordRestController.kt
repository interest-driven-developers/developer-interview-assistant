package com.idd.dia.infra.web

import com.idd.dia.application.domain.RecordType
import com.idd.dia.application.dto.InterviewRecordRequest
import com.idd.dia.application.dto.InterviewRecordResponse
import com.idd.dia.application.service.InterviewRecordService
import com.idd.dia.infra.security.LoginUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v0/interview-records")
class InterviewRecordRestController(
    private val interviewRecordService: InterviewRecordService
) {

    @PostMapping
    fun post(
        @LoginUser userPk: Long,
        @RequestBody request: InterviewRecordRequest
    ): ResponseEntity<ApiResponse<InterviewRecordResponse>> {

        val response = interviewRecordService.post(
            userPk = userPk, request = request
        )
        return ResponseEntity.ok(
            ApiResponse.success(data = response)
        )
    }

    @GetMapping("/{recordPk}")
    fun get(
        @LoginUser userPk: Long,
        @PathVariable recordPk: Long
    ): ResponseEntity<ApiResponse<InterviewRecordResponse>> {

        val response = interviewRecordService.get(pk = recordPk, userPk = userPk)
        return ResponseEntity.ok(
            ApiResponse.success(data = response)
        )
    }

    @GetMapping
    fun getPage(
        @LoginUser userPk: Long,
        @PageableDefault pageable: Pageable,
        @RequestParam recordType: RecordType? = null,
        @RequestParam questionPk: Long? = null,
    ): ResponseEntity<ApiResponse<Page<InterviewRecordResponse>>> {

        val response = interviewRecordService.getPage(
            userPk = userPk, recordType = recordType, questionPk = questionPk, pageable = pageable
        )
        return ResponseEntity.ok(
            ApiResponse.success(data = response)
        )
    }

    @DeleteMapping("/{recordPk}")
    fun delete(
        @LoginUser userPk: Long,
        @PathVariable recordPk: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        interviewRecordService.delete(pk = recordPk, userPk = userPk)
        return ResponseEntity.ok(
            ApiResponse.success(data = null)
        )
    }
}
