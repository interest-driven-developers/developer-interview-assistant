package com.idd.dia.application.dto

import com.idd.dia.application.domain.InterviewQuestion

data class InterviewQuestionResponse(
    val pk: Long,
    val title: String
) {
    constructor(requestUserPk: Long?, question: InterviewQuestion) : this(
        pk = question.pk,
        title = question.getTitle(requestUserPk)
    )
}

data class InterviewQuestionRequest(
    val title: String
) {
    fun toEntity(requestUserPk: Long): InterviewQuestion {
        return InterviewQuestion(userPk = requestUserPk, title = title)
    }
}
