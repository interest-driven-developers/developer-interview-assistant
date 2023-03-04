package com.idd.dia.application.domain

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity

interface InterviewQuestionVoiceRepository : JpaRepository<InterviewQuestionVoice, Long>

@Entity
class InterviewQuestionVoice(
    @Column(nullable = false, updatable = false)
    val questionPk: Long,

    @Column
    val url: String,

    pk: Long = 0L
) : BaseEntity(pk = pk)
