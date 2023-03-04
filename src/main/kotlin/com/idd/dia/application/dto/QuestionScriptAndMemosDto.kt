package com.idd.dia.application.dto

import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionMemo
import com.idd.dia.application.domain.InterviewScript
import com.idd.dia.application.domain.MemoContent

data class QuestionScriptAndMemosDto(
    val question: String,
    val script: String?,
    val memos: List<MemoContent>
) {
    companion object {
        fun of(
            requestUserPk: Long,
            question: InterviewQuestion,
            script: InterviewScript?,
            memos: List<InterviewQuestionMemo>
        ): QuestionScriptAndMemosDto {
            return QuestionScriptAndMemosDto(
                question = question.getTitle(requestUserPk),
                script = script?.getContent(requestUserPk),
                memos = memos.map { it.getContent(requestUserPk) }
            )
        }
    }
}
