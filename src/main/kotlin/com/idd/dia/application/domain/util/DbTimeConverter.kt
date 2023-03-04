package com.idd.dia.application.domain.util

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
object DbTimeConverter : AttributeConverter<LocalDateTime, Timestamp> {

    override fun convertToDatabaseColumn(attribute: LocalDateTime?): Timestamp? {
        return attribute?.run { Timestamp.valueOf(this) }
    }

    override fun convertToEntityAttribute(dbData: Timestamp?): LocalDateTime? {
        val zone = "Asia/Seoul"
        val offset = ZonedDateTime.now(ZoneId.of(zone)).offset.totalSeconds
        return dbData?.toLocalDateTime()?.plusSeconds(offset.toLong())
    }
}
