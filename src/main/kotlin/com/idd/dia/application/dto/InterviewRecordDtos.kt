package com.idd.dia.application.dto

import com.idd.dia.application.domain.InterviewRecord
import com.idd.dia.application.domain.RecordType
import java.time.LocalDateTime

data class InterviewRecordRequest(
    val questionPk: Long,
    val type: RecordType,
    val content: String
) {
    fun toEntity(userPk: Long): InterviewRecord {
        return InterviewRecord(
            userPk = userPk,
            questionPk = questionPk,
            type = type,
            content = content
        )
    }
}

data class InterviewRecordResponse(
    val pk: Long,
    val questionPk: Long,
    val type: RecordType,
    val content: String,
    val createAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    constructor(record: InterviewRecord, userPk: Long) : this(
        pk = record.pk,
        questionPk = record.questionPk,
        type = record.type,
        content = record.getContent(userPk),
        createAt = record.createAt,
        updatedAt = record.updateAt
    )
}
