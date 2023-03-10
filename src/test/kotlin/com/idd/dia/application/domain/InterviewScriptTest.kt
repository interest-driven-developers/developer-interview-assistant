package com.idd.dia.application.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

/**
 * @see InterviewScript
 */
class InterviewScriptTest : ExpectSpec({

    context("스크립트 조회") {
        val ownerPk = 1L
        val notOwnerPk = 2L
        val scriptContent = "script content"
        val script = InterviewScript(
            userPk = ownerPk,
            questionPk = 1L,
            content = scriptContent,
            pk = 1L
        )

        expect("스크립트 작성자는 조회할 수 있다.") {
            script.getContent(ownerPk) shouldBe scriptContent
        }

        expect("스크립트 작성자가 아니면 조회할 수 없다. ") {
            shouldThrowExactly<IllegalArgumentException> {
                script.getContent(notOwnerPk)
            }
        }
    }

    context("스크립트 수정") {
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
                script.updateContent(requestUserPk = ownerPk, newScript = newScriptContent)
            }
            script.getContent(requestUserPk = ownerPk) shouldBe newScriptContent
        }

        expect("스크립트 주인이 아니면 수정할 수 없다") {
            shouldThrowExactly<IllegalArgumentException> {
                script.updateContent(requestUserPk = 2L, newScript = newScriptContent)
            }
        }
    }
})
