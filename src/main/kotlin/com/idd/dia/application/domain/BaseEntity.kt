package com.idd.dia.application.domain

import com.idd.dia.application.domain.util.DbTimeConverter
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Convert
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val pk: Long = 0L,

    @Convert(converter = DbTimeConverter::class)
    var createAt: LocalDateTime = LocalDateTime.now(),

    @Convert(converter = DbTimeConverter::class)
    var updateAt: LocalDateTime? = null
)
