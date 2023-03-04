package com.idd.dia.application.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

/**
 * @see InterviewScript
 */
class InterviewScriptTest : ExpectSpec({

    context("스크립트는 작성자만 수정할 수 있다") {
        val ownerPk = 1L
        val script = InterviewScript(
            userPk = ownerPk,
            questionPk = 1L,
            content = "script",
            pk = 1L
        )
        val newScriptContent = "newScript"

        expect("스크립트 주인은 수정할 수 있다") {
            shouldNotThrowAny {
                script.updateScript(requestUserPk = ownerPk, newScript = newScriptContent)
            }
            script.getContent(requestUserPk = ownerPk) shouldBe newScriptContent
        }

        expect("스크립트 주인이 아니면 수정할 수 없다") {
            shouldThrowExactly<IllegalArgumentException> {
                script.updateScript(requestUserPk = 2L, newScript = newScriptContent)
            }
        }
    }
})
