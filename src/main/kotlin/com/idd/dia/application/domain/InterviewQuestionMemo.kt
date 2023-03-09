package com.idd.dia.application.domain

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity

interface InterviewQuestionMemoRepository : JpaRepository<InterviewQuestionMemo, Long> {
    fun findAllByUserPkAndQuestionPkAndDeletedIsFalse(userPk: Long, questionPk: Long): List<InterviewQuestionMemo>
}

@Entity
class InterviewQuestionMemo(
    @Column(updatable = false, nullable = false)
    val userPk: Long,

    @Column(nullable = false, updatable = false)
    val questionPk: Long,

    @Column(nullable = false)
    private var content: MemoContent,

    @Column(nullable = false)
    private var deleted: Boolean = false,

    pk: Long = 0L
) : BaseEntity(pk = pk) {

    fun verify(requestUserPk: Long) {
        check(!deleted) { "삭제된 컨텐츠입니다." }
        if (userPk != requestUserPk) throw IllegalArgumentException("권한이 없습니다.")
    }

    fun getContent(requestUserPk: Long): MemoContent {
        this.verify(requestUserPk)
        return content
    }

    fun updateContent(requestUserPk: Long, memoContent: MemoContent) {
        this.verify(requestUserPk)
        this.content = memoContent
    }

    fun delete(requestUserPk: Long) {
        this.verify(requestUserPk)
        this.deleted = true
    }
}
