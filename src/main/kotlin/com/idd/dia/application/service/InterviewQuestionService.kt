package com.idd.dia.application.service

import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.application.dto.InterviewQuestionRequest
import com.idd.dia.application.dto.InterviewQuestionResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InterviewQuestionService(
    private val interviewQuestionRepository: InterviewQuestionRepository,
) {

    @Transactional(readOnly = true)
    fun get(pk: Long, userPk: Long?): InterviewQuestionResponse? {
        return interviewQuestionRepository.findByIdOrNull(pk)?.run {
            InterviewQuestionResponse(requestUserPk = userPk, this)
        }
    }

    @Transactional(readOnly = true)
    fun get(pageable: Pageable): Page<InterviewQuestionResponse> {
        return interviewQuestionRepository.findByUserPkIsNull(pageable)
            .map { InterviewQuestionResponse(requestUserPk = null, question = it) }
    }

    @Transactional(readOnly = true)
    fun getMy(userPk: Long, pageable: Pageable): Page<InterviewQuestionResponse> {
        return interviewQuestionRepository.findByUserPkAndDeletedIsFalse(userPk, pageable)
            .map { InterviewQuestionResponse(requestUserPk = userPk, question = it) }
    }

    @Transactional
    fun post(userPk: Long, request: InterviewQuestionRequest): InterviewQuestionResponse {
        val newQuestion = request.toEntity(userPk)
        return interviewQuestionRepository.save(newQuestion).run {
            InterviewQuestionResponse(requestUserPk = userPk, question = this)
        }
    }
}
