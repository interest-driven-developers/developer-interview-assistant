package com.idd.dia.application.service

import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.application.domain.InterviewScriptRepository
import com.idd.dia.application.dto.InterviewScriptRequest
import com.idd.dia.application.dto.InterviewScriptResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InterviewScriptService(
    private val interviewScriptRepository: InterviewScriptRepository,
    private val interviewQuestionRepository: InterviewQuestionRepository
) {
    @Transactional
    fun postScript(
        requestUserPk: Long,
        questionPk: Long,
        interviewScriptRequest: InterviewScriptRequest
    ): InterviewScriptResponse {
        require(interviewQuestionRepository.existsByPkAndDeletedIsFalse(questionPk)) { "삭제되었거나 없는 문항 번호입니다." }
        require(
            interviewScriptRepository.existsByUserPkAndQuestionPk(userPk = requestUserPk, questionPk = questionPk)
        ) { "이미 스크립트가 존재합니다." }

        val newScript = interviewScriptRequest.toEntity(requestUserPk = requestUserPk, questionPk = questionPk)
        return interviewScriptRepository.save(newScript)
            .run { InterviewScriptResponse(requestUserPk, this) }
    }

    @Transactional(readOnly = true)
    fun getScript(requestUserPk: Long, questionPk: Long): InterviewScriptResponse? {
        return interviewScriptRepository.findByUserPkAndQuestionPk(requestUserPk, questionPk)
            ?.run { InterviewScriptResponse(requestUserPk, this) }
    }

    @Transactional(readOnly = true)
    fun getScripts(requestUserPk: Long, pageable: Pageable): Page<InterviewScriptResponse> {
        return interviewScriptRepository.findByUserPk(userPk = requestUserPk, pageable = pageable).run {
            this.map { InterviewScriptResponse(requestUserPk, it) }
        }
    }

    @Transactional
    fun updateScript(
        requestUserPk: Long,
        scriptPk: Long,
        interviewScriptRequest: InterviewScriptRequest
    ): InterviewScriptResponse? {
        return interviewScriptRepository.findByIdOrNull(scriptPk)
            ?.run {
                this.updateContent(requestUserPk, interviewScriptRequest.content)
                InterviewScriptResponse(requestUserPk, this)
            }
    }
}
