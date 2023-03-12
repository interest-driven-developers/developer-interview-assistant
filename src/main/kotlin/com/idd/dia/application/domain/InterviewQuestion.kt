package com.idd.dia.application.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity

interface InterviewQuestionRepository : JpaRepository<InterviewQuestion, Long> {
    fun existsByPkAndDeletedIsFalse(pk: Long): Boolean
    fun findByUserPkAndDeletedIsFalse(userPk: Long, pageable: Pageable): Page<InterviewQuestion>
    fun findByUserPkIsNull(pageable: Pageable): Page<InterviewQuestion>
}

@Entity
class InterviewQuestion(
    @Column(updatable = false)
    val userPk: Long?,

    @Column(nullable = false)
    private var title: String,

    @Column(nullable = false)
    private var deleted: Boolean = false,

    pk: Long = 0L
) : BaseEntity(pk = pk) {

    fun verify(requestUserPk: Long?) {
        check(!deleted) { "삭제된 컨텐츠입니다." }
        userPk?.run {
            if (requestUserPk == null) throw IllegalArgumentException("먼저 로그인해주세요.")
            if (userPk != requestUserPk) throw IllegalArgumentException("권한이 없습니다.")
        }
    }

    fun getTitle(requestUserPk: Long?): String {
        this.verify(requestUserPk)
        return title
    }
}
