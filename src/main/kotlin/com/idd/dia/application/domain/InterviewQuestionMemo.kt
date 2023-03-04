package com.idd.dia.application.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import javax.persistence.Column
import javax.persistence.Entity

fun InterviewQuestionMemoRepository.deleteByPk(pk: Long, userPk: Long) {
    findByIdOrNull(pk)?.run {
        delete(pk)
    }
}

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

    private var deleted: Boolean = false,

    pk: Long = 0L
) : BaseEntity(pk = pk) {

    fun verifyOwner(requestUserPk: Long) {
        if (userPk != requestUserPk) throw IllegalArgumentException("권한이 없습니다.")
    }

    fun getContent(requestUserPk: Long): MemoContent {
        this.verifyOwner(requestUserPk)
        return content
    }

    fun updateContent(requestUserPk: Long, memoContent: MemoContent) {
        this.verifyOwner(requestUserPk)
        this.content = memoContent
    }

    fun delete(requestUserPk: Long) {
        this.verifyOwner(requestUserPk)
        this.deleted = true
    }
}
