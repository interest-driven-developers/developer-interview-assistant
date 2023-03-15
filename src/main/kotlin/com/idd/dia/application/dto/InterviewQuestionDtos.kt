package com.idd.dia.application.dto

import com.idd.dia.application.domain.InterviewQuestion
import java.time.LocalDateTime

data class InterviewQuestionResponse(
    val pk: Long,
    val title: String,
    val createAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    constructor(requestUserPk: Long?, question: InterviewQuestion) : this(
        pk = question.pk,
        title = question.getTitle(requestUserPk),
        createAt = question.createAt,
        updatedAt = question.updateAt
    )
}

data class InterviewQuestionRequest(
    val title: String
) {
    fun toEntity(requestUserPk: Long): InterviewQuestion {
        return InterviewQuestion(userPk = requestUserPk, title = title)
    }
}
