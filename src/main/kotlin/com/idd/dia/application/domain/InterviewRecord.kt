package com.idd.dia.application.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity

interface InterviewRecordRepository : JpaRepository<InterviewRecord, Long> {
    fun findByUserPk(userPk: Long, pageable: Pageable): Page<InterviewQuestion>
}

@Entity
class InterviewRecord(
    @Column(updatable = false, nullable = false)
    val userPk: Long,

    @Column(nullable = false, updatable = false)
    val questionPk: Long,

    @Column(length = 2000)
    private var content: String,

    @Column(nullable = false)
    private var deleted: Boolean = false,

    pk: Long = 0L
) : BaseEntity(pk = pk) {

    fun verify(requestUserPk: Long) {
        check(!deleted) { "삭제된 컨텐츠입니다." }
        require(userPk == requestUserPk) { "권한이 없습니다." }
    }

    fun getContent(requestUserPk: Long): String {
        this.verify(requestUserPk)
        return content
    }

    fun updateScript(requestUserPk: Long, newScript: String) {
        this.verify(requestUserPk)
        this.content = newScript
    }

    fun delete(requestUserPk: Long) {
        this.verify(requestUserPk)
        this.deleted = true
    }
}
