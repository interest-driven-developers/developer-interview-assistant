package com.idd.dia.application.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity

interface InterviewScriptRepository : JpaRepository<InterviewScript, Long> {

    fun findByUserPkAndQuestionPk(userPk: Long, questionPk: Long): InterviewScript?
    fun findByUserPk(userPk: Long, pageable: Pageable): Page<InterviewScript>
}

@Entity
class InterviewScript(
    @Column(updatable = false, nullable = false)
    val userPk: Long,

    @Column(nullable = false, updatable = false)
    val questionPk: Long,

    @Column(length = 2000)
    private var content: String,

    pk: Long = 0L
) : BaseEntity(pk = pk) {

    fun verifyOwner(requestUserPk: Long) {
        if (userPk != requestUserPk) throw IllegalArgumentException("권한이 없습니다.")
    }

    fun getContent(requestUserPk: Long): String {
        this.verifyOwner(requestUserPk)
        return content
    }

    fun updateContent(requestUserPk: Long, newScript: String) {
        this.verifyOwner(requestUserPk)
        this.content = newScript
    }
}
