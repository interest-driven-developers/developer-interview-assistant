package com.idd.dia.application.dto

import com.idd.dia.application.domain.InterviewQuestionMemo
import com.idd.dia.application.domain.MemoContent

data class InterviewQuestionMemosResponse(
    val memos: List<InterviewQuestionMemoResponse>
) {
    companion object {
        fun of(requestUserPk: Long, memos: List<InterviewQuestionMemo>): InterviewQuestionMemosResponse {
            return InterviewQuestionMemosResponse(
                memos.map { InterviewQuestionMemoResponse(requestUserPk, it) }
            )
        }
    }
}

data class InterviewQuestionMemoResponse(
    val pk: Long,
    val content: MemoContent
) {
    constructor(requestUserPk: Long, memo: InterviewQuestionMemo) : this(
        pk = memo.pk,
        content = memo.getContent(requestUserPk)
    )
}

data class InterviewQuestionMemoRequest(
    val content: MemoContent
) {
    fun toEntity(requestUserPk: Long, questionPk: Long): InterviewQuestionMemo {
        return InterviewQuestionMemo(userPk = requestUserPk, questionPk = questionPk, content = content)
    }
}
