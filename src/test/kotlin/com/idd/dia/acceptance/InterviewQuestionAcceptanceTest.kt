package com.idd.dia.acceptance

import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.application.domain.User
import com.idd.dia.infra.security.JwtTokenProvider
import com.idd.dia.infra.web.InterviewQuestionRestController
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

/**
 * @see InterviewQuestionRestController
 */
internal class InterviewQuestionAcceptanceTest : AcceptanceTest() {

    @Autowired
    lateinit var interviewQuestionRepository: InterviewQuestionRepository

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @BeforeEach
    fun beforeTest() {
        interviewQuestionRepository.deleteAll()
    }

    @Test
    fun `면접 질문 조회 - 유저에 귀속된 문제가 아니면 누구나 볼 수 있다`() {
        val title = "test title"
        val targetData = interviewQuestionRepository.save(
            InterviewQuestion(userPk = null, title = title)
        )

        val result = RestAssured.given(spec)
            .accept("application/json")
            .filter(
                document(
                    "(get)public-interview-question",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("data.pk").type(JsonFieldType.NUMBER).description("문제 pk"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("문제"),
                        fieldWithPath("message").type(JsonFieldType.NULL).description("알림 메세지"),
                    )
                )
            )
            .`when`().get("/api/v0/interview-questions/${targetData.pk}")
            .logAndReturn()

        with(result.jsonPath()) {
            getInt("data.pk") shouldBe targetData.pk
            getString("data.title") shouldBe title
        }
    }

    @Test
    fun `면접 질문 조회 - 유저에 귀속된 문제면 해당 유저만 볼 수 있다`() {
        val user = User("jaeykweon", pk = 2L)
        val jwt = jwtTokenProvider.createToken(user)
        val title = "test title"
        val targetData = interviewQuestionRepository.save(
            InterviewQuestion(userPk = 2L, title = title)
        )

        val result = RestAssured.given(spec)
            .accept("application/json")
            .filter(
                document(
                    "(get)private-interview-question",
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName("Authorization").description("Basic auth credentials")
                    ),
                    responseFields(
                        fieldWithPath("data.pk").type(JsonFieldType.NUMBER).description("문제 pk"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("문제"),
                        fieldWithPath("message").type(JsonFieldType.NULL).description("알림 메세지"),
                    )
                )
            )
            .header("Authorization", "Bearer ${jwt.token}")
            .`when`().get("/api/v0/interview-questions/${targetData.pk}")
            .logAndReturn()

        with(result.jsonPath()) {
            getInt("data.pk") shouldBe targetData.pk
            getString("data.title") shouldBe title
        }
    }
}
