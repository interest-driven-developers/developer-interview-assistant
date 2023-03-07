package com.idd.dia.acceptance

import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.infra.web.InterviewQuestionRestController
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

/**
 * @see InterviewQuestionRestController
 */
internal class InterviewQuestionAcceptanceTest : AcceptanceTest() {

    @Autowired
    lateinit var interviewQuestionRepository: InterviewQuestionRepository

    @BeforeEach
    fun beforeTest() {
        interviewQuestionRepository.deleteAll()
        interviewQuestionRepository.save(
            InterviewQuestion(userPk = null, title = "test title", pk = 2L)
        )
    }

    @Test
    fun test() {
        RestAssured.given(spec)
            .accept("application/json")
            .filter(document("interview-question"))
            .`when`().get("/api/v0/interview-questions/1")
            .then().assertThat().statusCode(200)
    }
}
