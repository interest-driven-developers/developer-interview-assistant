package com.idd.dia.acceptance

import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.infra.web.InterviewQuestionRestController
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
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
    }

    @Test
    fun `면접 질문 단건 조회 - 유저에 귀속된 문제가 아니면 누구나 볼 수 있다`() {
        val title = "test title"
        val targetData = interviewQuestionRepository.save(
            InterviewQuestion(userPk = null, title = title)
        )

        val result = RestAssured.given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
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
    fun `면접 질문 단건 조회 - 유저에 귀속된 문제면 해당 유저만 볼 수 있다`() {
        val title = "test title"
        val targetData = interviewQuestionRepository.save(
            InterviewQuestion(userPk = 2L, title = title)
        )

        val result = RestAssured.given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer $jwtToken")
            .filter(
                document(
                    "(get)private-interview-question",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("questionPk").description("문제 pk"),
                    ),
                    requestHeaders(
                        headerWithName("Authorization").description("JWT Bearer Token")
                    ),
                    responseFields(
                        fieldWithPath("data.pk").type(JsonFieldType.NUMBER).description("문제 pk"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("문제"),
                        fieldWithPath("message").type(JsonFieldType.NULL).description("알림 메세지"),
                    )
                )
            )
            .`when`().get("/api/v0/interview-questions/{questionPk}", targetData.pk)
            .logAndReturn()

        with(result.jsonPath()) {
            getInt("data.pk") shouldBe targetData.pk
            getString("data.title") shouldBe targetData.getTitle(loginUser.pk)
        }
    }

    @Test
    fun `면접 질문 리스트 조회 - 공용 문제 리스트`() {
        val targetData = interviewQuestionRepository.saveAll(
            listOf(
                InterviewQuestion(userPk = null, title = "test title1"),
                InterviewQuestion(userPk = null, title = "test title2")
            )
        )

        val result = RestAssured.given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .filter(
                document(
                    "(get)public-interview-questions",
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("page").description("page"),
                        parameterWithName("size").description("size")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("data.content[].pk").type(JsonFieldType.NUMBER).description("문제 pk"),
                        fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("문제"),
                        fieldWithPath("message").type(JsonFieldType.NULL).description("알림 메세지"),
                    ),
                )
            )
            .`when`().get("/api/v0/interview-questions?page=0&size=10")
            .logAndReturn()

        with(result.jsonPath()) {
            getList<String>("data.content").size shouldBe 2
            getInt("data.content[0].pk") shouldBe targetData[0].pk
            getString("data.content[0].title") shouldBe targetData[0].getTitle(null)
            getInt("data.content[1].pk") shouldBe targetData[1].pk
            getString("data.content[1].title") shouldBe targetData[1].getTitle(null)
        }
    }
}
