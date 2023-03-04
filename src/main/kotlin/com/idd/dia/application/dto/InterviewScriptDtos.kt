package com.idd.dia.application.dto

import com.idd.dia.application.domain.InterviewScript
import java.time.LocalDateTime

data class InterviewScriptResponse(
    val pk: Long,
    val questionPk: Long,
    val content: String,
    val createAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    constructor(requestUserPk: Long, script: InterviewScript) : this(
        pk = script.pk,
        questionPk = script.questionPk,
        content = script.getContent(requestUserPk),
        createAt = script.createAt,
        updatedAt = script.updateAt
    )
}

data class InterviewScriptRequest(
    val content: String
) {
    fun toEntity(requestUserPk: Long, questionPk: Long): InterviewScript {
        return InterviewScript(
            userPk = requestUserPk, questionPk = questionPk, content = content
        )
    }
}
