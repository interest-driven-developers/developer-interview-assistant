package com.idd.dia.application.service

import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.application.dto.InterviewQuestionRequest
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

/**
 * @see InterviewQuestionService
 */
@DataJpaTest
class InterviewQuestionServiceTest(
    private val interviewQuestionRepository: InterviewQuestionRepository
) : ExpectSpec({

    val interviewQuestionService = InterviewQuestionService(interviewQuestionRepository)

    beforeSpec {
        val userPk = 1L
        val title = "test title"
        val newEntity = InterviewQuestion(
            userPk = userPk, title = title
        )
        interviewQuestionRepository.save(newEntity)
    }

    context("get") {

        expect("get one") {
            val test =  interviewQuestionService.get(1L, 1L)!!
            test.pk shouldBe 1
            test.title shouldBe "test title"
        }
    }


    context("post") {

        val userPk = 1L
        val title = "test title2"
        val request = InterviewQuestionRequest(title = title)

        expect("post one") {

            interviewQuestionService.post(userPk = userPk,request = request)

            val test =  interviewQuestionService.get(2L, userPk)!!
            test.pk shouldBe 2
            test.title shouldBe "test title2"
        }
    }

})
