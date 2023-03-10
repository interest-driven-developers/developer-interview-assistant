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
        return interviewQuestionRepository.findByUserPkIsNull(pageable).run {
            this.map { InterviewQuestionResponse(requestUserPk = null, it) }
        }
    }

    @Transactional(readOnly = true)
    fun getMy(userPk: Long, pageable: Pageable): Page<InterviewQuestionResponse> {
        return interviewQuestionRepository.findByUserPkAndDeletedIsFalse(userPk, pageable).run {
            this.map {
                InterviewQuestionResponse(requestUserPk = userPk, it)
            }
        }
    }

    @Transactional
    fun post(userPk: Long, request: InterviewQuestionRequest): InterviewQuestionResponse {
        val newQuestion = request.toEntity(userPk)
        return interviewQuestionRepository.save(newQuestion).run {
            InterviewQuestionResponse(requestUserPk = userPk, this)
        }
    }
}
