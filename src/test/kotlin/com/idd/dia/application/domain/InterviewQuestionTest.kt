package com.idd.dia.application.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

/**
 * @see InterviewQuestion
 */
class InterviewQuestionTest : ExpectSpec({

    context("문제 조회") {
        val questionTitle = "문제 제목"
        val publicQuestion = InterviewQuestion(userPk = null, title = questionTitle, pk = 1L)
        val ownerPk = 1L
        val userCreatedQuestion = InterviewQuestion(userPk = ownerPk, title = questionTitle, pk = 2L)
        val deletedQuestion = InterviewQuestion(userPk = ownerPk, title = questionTitle, deleted = true, pk = 3L)

        expect("문제 주인은 볼 수 있다.") {
            shouldNotThrowAny {
                userCreatedQuestion.getTitle(ownerPk) shouldBe questionTitle
            }
        }
        expect("문제 주인이 아니면 볼 수 없다.") {
            shouldThrowExactly<IllegalArgumentException> {
                userCreatedQuestion.getTitle(null)
            }
            shouldThrowExactly<IllegalArgumentException> {
                userCreatedQuestion.getTitle(2L)
            }
        }

        expect("유저에 귀속되지 않은 문제는 누구나 볼 수 있다.") {
            shouldNotThrowAny {
                publicQuestion.getTitle(null) shouldBe questionTitle
                publicQuestion.getTitle(3L) shouldBe questionTitle
            }
        }

        expect("삭제된 문제는 아무도 볼 수 없다") {
            shouldThrowExactly<IllegalStateException> {
                deletedQuestion.getTitle(ownerPk)
            }
        }
    }
})
