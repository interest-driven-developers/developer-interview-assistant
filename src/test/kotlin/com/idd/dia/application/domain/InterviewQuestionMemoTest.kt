package com.idd.dia.application.domain

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

/**
 * @see InterviewQuestionMemo
 */
class InterviewQuestionMemoTest : ExpectSpec({

    context("작성한 메모는 작성자만 볼 수 있다.") {
        val ownerPk = 1L
        val memoContent = MemoContent("content")
        val interviewQuestionMemo = InterviewQuestionMemo(
            userPk = ownerPk,
            questionPk = 1L,
            content = memoContent
        )

        expect("작성자는 볼 수 있다.") {
            interviewQuestionMemo.getContent(requestUserPk = ownerPk) shouldBe memoContent
        }

        expect("작성자가 아니면 볼 수 있다.") {
            shouldThrowExactly<IllegalArgumentException> {
                interviewQuestionMemo.getContent(requestUserPk = 2L)
            }
        }
    }
})
