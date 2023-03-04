package com.idd.dia.application.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ExpectSpec

/**
 * @see InterviewQuestion
 */
class InterviewQuestionTest : ExpectSpec({

    context("유저에 귀속된 문제는 해당 유저만 볼 수 있다.") {
        val ownerPk = 1L
        val question = InterviewQuestion(userPk = ownerPk, title = "문제 제목", pk = 1L)

        expect("문제 주인은 볼 수 있다.") {
            shouldNotThrowAny { question.getTitle(ownerPk) }
        }
        expect("문제 주인이 아니면 볼 수 없다.") {
            shouldThrowExactly<IllegalArgumentException> {
                question.getTitle(2L)
            }
        }
    }

    expect("유저에 귀속되지 않은 문제는 누구나 볼 수 있다.") {
        val question = InterviewQuestion(userPk = null, title = "문제 제목", pk = 1L)
        shouldNotThrowAny { question.getTitle(1L) }
    }
})
