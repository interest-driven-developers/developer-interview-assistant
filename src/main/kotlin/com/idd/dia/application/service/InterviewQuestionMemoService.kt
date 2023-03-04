package com.idd.dia.application.service

import com.idd.dia.application.domain.InterviewQuestionMemoRepository
import com.idd.dia.application.domain.MemoContent
import com.idd.dia.application.dto.InterviewQuestionMemoRequest
import com.idd.dia.application.dto.InterviewQuestionMemoResponse
import com.idd.dia.application.dto.InterviewQuestionMemosResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InterviewQuestionMemoService(
    private val memoRepository: InterviewQuestionMemoRepository
) {

    @Transactional(readOnly = true)
    fun getAll(userPk: Long, questionPk: Long): InterviewQuestionMemosResponse {
        return memoRepository.findAllByUserPkAndQuestionPkAndDeletedIsFalse(userPk, questionPk).run {
            InterviewQuestionMemosResponse.of(userPk, this)
        }
    }

    @Transactional
    fun post(userPk: Long, questionPk: Long, request: InterviewQuestionMemoRequest): InterviewQuestionMemoResponse {

        val newMemo = request.toEntity(requestUserPk = userPk, questionPk = questionPk)
        return memoRepository.save(newMemo).run {
            InterviewQuestionMemoResponse(userPk, this)
        }
    }

    @Transactional
    fun update(userPk: Long, memoPk: Long, newContent: MemoContent): InterviewQuestionMemoResponse? {
        return memoRepository.findByIdOrNull(memoPk)?.run {
            this.updateContent(requestUserPk = userPk, memoContent = newContent)
            InterviewQuestionMemoResponse(userPk, this)
        }
    }

    @Transactional
    fun delete(userPk: Long, memoPk: Long) {
        memoRepository.findByIdOrNull(memoPk)?.run {
            this.delete(userPk)
        } ?: throw IllegalArgumentException("memo not found")
    }
}
