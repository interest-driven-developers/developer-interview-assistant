package com.idd.dia.application.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity

interface InterviewQuestionRepository : JpaRepository<InterviewQuestion, Long> {
    fun existsByPk(pk: Long): Boolean
    fun findByUserPk(userPk: Long, pageable: Pageable): Page<InterviewQuestion>
    fun findByUserPkIsNull(pageable: Pageable): Page<InterviewQuestion>
}

@Entity
class InterviewQuestion(
    @Column(updatable = false)
    val userPk: Long?,

    @Column(nullable = false)
    private var title: String,

    pk: Long = 0L
) : BaseEntity(pk = pk) {

    fun verifyOwner(requestUserPk: Long?) {
        userPk?.run {
            if (requestUserPk == null) throw IllegalArgumentException("먼저 로그인해주세요.")
            if (userPk != requestUserPk) throw IllegalArgumentException("권한이 없습니다.")
        }
    }

    fun getTitle(requestUserPk: Long?): String {
        verifyOwner(requestUserPk)
        return title
    }
}
