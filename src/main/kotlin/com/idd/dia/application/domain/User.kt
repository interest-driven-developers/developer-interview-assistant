package com.idd.dia.application.domain

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Table

interface UserRepository : JpaRepository<User, Long> {
    fun findByGithubId(githubId: String): User?
}

@Table(name = "USERS")
@Entity
class User(
    val githubId: String,
    pk: Long = 0L
) : BaseEntity(pk = pk)
