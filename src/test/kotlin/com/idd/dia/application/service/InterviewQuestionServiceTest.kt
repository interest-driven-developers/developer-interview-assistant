package com.idd.dia.application.service

import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.application.dto.InterviewQuestionRequest
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull

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

    context("공용 인터뷰 질문 조회") {

        expect("한 개 조회") {
            val questionDto = interviewQuestionService.get(1L, 1L)!!
            questionDto.title shouldBe "test title"
        }
    }

    context("인터뷰 질문 등록") {

        val userPk = 1L
        val title = "test title2"
        val request = InterviewQuestionRequest(title = title)

        expect("유저가 질문을 등록하면 해당 유저에 귀속된다.") {
            val newQuestion = interviewQuestionService.post(userPk = userPk, request = request)

            val newQuestionEntity = interviewQuestionRepository.findByIdOrNull(newQuestion.pk)!!

            newQuestionEntity.userPk shouldBe userPk
            newQuestionEntity.getTitle(userPk) shouldBe "test title2"
        }
    }
})
