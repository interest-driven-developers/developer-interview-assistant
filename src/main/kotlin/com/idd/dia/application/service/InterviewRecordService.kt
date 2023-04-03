package com.idd.dia.application.service

import com.idd.dia.application.domain.InterviewRecordRepository
import com.idd.dia.application.domain.RecordType
import com.idd.dia.application.dto.InterviewRecordRequest
import com.idd.dia.application.dto.InterviewRecordResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InterviewRecordService(
    private val interviewRecordRepository: InterviewRecordRepository
) {

    @Transactional
    fun post(userPk: Long, request: InterviewRecordRequest): InterviewRecordResponse {
        interviewRecordRepository.save(
            request.toEntity(userPk)
        ).run {
            return InterviewRecordResponse(this, userPk = userPk)
        }
    }

    /**
     * 단 건 조회 by pk
     */
    @Transactional(readOnly = true)
    fun get(pk: Long, userPk: Long): InterviewRecordResponse {
        return interviewRecordRepository.findByPkAndDeletedIsFalse(pk)?.run {
            InterviewRecordResponse(record = this, userPk = userPk)
        } ?: throw IllegalArgumentException("이미 삭제되었거나 없는 데이터입니다.")
    }

    @Transactional(readOnly = true)
    fun getPage(userPk: Long, questionPk: Long?, recordType: RecordType?, pageable: Pageable): Page<InterviewRecordResponse> {
        if (recordType == null && questionPk == null) {
            return this.getPage(userPk, pageable)
        }

        if (recordType != null && questionPk == null) {
            return this.getPage(userPk = userPk, recordType = recordType, pageable = pageable)
        }

        if (recordType == null && questionPk != null) {
            return this.getPage(userPk = userPk, questionPk = questionPk, pageable = pageable)
        }

        if (recordType != null && questionPk != null) {
            return this.getPage(userPk = userPk, type = recordType, questionPk = questionPk, pageable = pageable)
        }

        return Page.empty(pageable)
    }

    /**
     * 리스트 조회 by 유저
     */
    private fun getPage(userPk: Long, pageable: Pageable): Page<InterviewRecordResponse> {
        return interviewRecordRepository.findByUserPkAndDeletedIsFalse(userPk = userPk, pageable = pageable)
            .map { InterviewRecordResponse(it, userPk) }
    }

    /**
     * 리스트 조회 by 유저 & 타입
     */
    private fun getPage(userPk: Long, recordType: RecordType, pageable: Pageable): Page<InterviewRecordResponse> {
        return interviewRecordRepository.findByUserPkAndTypeAndDeletedIsFalse(
            userPk = userPk, type = recordType, pageable = pageable
        ).map { InterviewRecordResponse(record = it, userPk = userPk) }
    }

    /**
     * 리스트 조회 by 유저 & 문제
     */
    private fun getPage(userPk: Long, questionPk: Long, pageable: Pageable): Page<InterviewRecordResponse> {
        return interviewRecordRepository.findByUserPkAndQuestionPkAndDeletedIsFalse(
            userPk = userPk, questionPk = questionPk, pageable = pageable
        ).map {
            InterviewRecordResponse(record = it, userPk = userPk)
        }
    }

    /**
     * 리스트 조회 by 유저 & 타입 & 문제
     */
    private fun getPage(userPk: Long, type: RecordType, questionPk: Long, pageable: Pageable): Page<InterviewRecordResponse> {
        return interviewRecordRepository.findByUserPkAndTypeAndQuestionPkAndDeletedIsFalse(
            userPk = userPk, type = type, questionPk = questionPk, pageable = pageable
        ).map {
            InterviewRecordResponse(record = it, userPk = userPk)
        }
    }

    @Transactional
    fun delete(pk: Long, userPk: Long) {
        interviewRecordRepository.findByPkAndDeletedIsFalse(pk)?.run {
            this.delete(requestUserPk = userPk)
        }
    }
}
