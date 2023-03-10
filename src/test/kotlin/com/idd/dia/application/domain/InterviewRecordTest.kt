package com.idd.dia.application.domain

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

/**
 * @see InterviewRecord
 */
class InterviewRecordTest : ExpectSpec({

    context("면접 레코드 조회") {
        val ownerPk = 1L
        val notOwnerPk = 2L
        val questionPk = 1L
        val content = "test record"
        val record = InterviewRecord(
            userPk = ownerPk,
            questionPk = questionPk,
            type = RecordType.PRACTICE,
            content = content
        )
        val deletedRecord = InterviewRecord(
            userPk = ownerPk,
            questionPk = questionPk,
            content = content,
            type = RecordType.PRACTICE,
            deleted = true
        )

        expect("주인은 볼 수 있다.") {
            record.getContent(ownerPk) shouldBe content
        }

        expect("주인이 아니면 볼 수 없다") {
            shouldThrowExactly<IllegalArgumentException> {
                record.getContent(notOwnerPk)
            }
        }

        expect("삭제된 레코드는 누구도 볼 수 없다.") {
            shouldThrowExactly<IllegalStateException> {
                deletedRecord.getContent(ownerPk)
            }
            shouldThrowExactly<IllegalStateException> {
                deletedRecord.getContent(notOwnerPk)
            }
        }
    }

    context("면접 레코드 삭제") {
        val ownerPk = 1L
        val notOwnerPk = 2L
        val questionPk = 1L

        val content = "test record"
        val record = InterviewRecord(
            userPk = ownerPk,
            questionPk = questionPk,
            type = RecordType.MOCK,
            content = content,
        )

        expect("주인은 삭제할 수 있다") {
            record.delete(ownerPk)

            shouldThrowExactly<IllegalStateException> { record.getContent(ownerPk) }
        }

        expect("주인 외에는 삭제할 수 없다.") {
            shouldThrowExactly<IllegalArgumentException> { record.delete(notOwnerPk) }
        }

        expect("이미 삭제된 히스토리를 삭제하려 할 경우 에러가 발생한다") {
            record.delete(ownerPk)

            shouldThrowExactly<IllegalStateException> { record.delete(notOwnerPk) }
        }
    }
})
