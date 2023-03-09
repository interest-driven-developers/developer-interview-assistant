package com.idd.dia.application.domain

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

interface UserRepository : JpaRepository<User, Long> {
    fun findByGithubId(githubId: String): User?
}

@Table(name = "USERS")
@Entity
class User(
    val githubId: String,

    @Column(nullable = false)
    private var deleted: Boolean = false,

    pk: Long = 0L
) : BaseEntity(pk = pk)
