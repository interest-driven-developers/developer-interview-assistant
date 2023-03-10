package com.idd.dia.application.domain

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

/**
 * @see InterviewQuestionMemo
 */
class InterviewQuestionMemoTest : ExpectSpec({

    context("메모 조회") {
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

    context("메모 수정") {
        val ownerPk = 1L
        val notOwnerPk = 2L
        val memoContent = MemoContent("content")
        val interviewQuestionMemo = InterviewQuestionMemo(
            userPk = ownerPk,
            questionPk = 1L,
            content = memoContent
        )
        val newContent = MemoContent("new content")

        expect("작성자는 수정할 수 있다.") {
            interviewQuestionMemo.updateContent(requestUserPk = ownerPk, memoContent = newContent)

            interviewQuestionMemo.getContent(requestUserPk = ownerPk) shouldBe newContent
        }

        expect("작성자가 아니면 수정할 수 없다") {
            shouldThrowExactly<IllegalArgumentException> {
                interviewQuestionMemo.updateContent(requestUserPk = notOwnerPk, memoContent = newContent)
            }
        }
    }

    context("메모 삭제") {
        val ownerPk = 1L
        val notOwnerPk = 2L
        val memoContent = MemoContent("content")
        val interviewQuestionMemo = InterviewQuestionMemo(
            userPk = ownerPk,
            questionPk = 1L,
            content = memoContent
        )

        expect("작성자는 삭제할 수 있다.") {
            interviewQuestionMemo.delete(requestUserPk = ownerPk)

            shouldThrowExactly<IllegalStateException> {
                interviewQuestionMemo.getContent(requestUserPk = ownerPk)
            }
        }

        expect("작성자가 아니면 삭제할 수 없다.") {
            shouldThrowExactly<IllegalArgumentException> {
                interviewQuestionMemo.delete(requestUserPk = notOwnerPk)
            }
        }
    }
})
