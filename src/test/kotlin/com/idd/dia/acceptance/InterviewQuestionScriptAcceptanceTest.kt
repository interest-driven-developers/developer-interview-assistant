package com.idd.dia.acceptance

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.application.domain.InterviewScript
import com.idd.dia.application.domain.InterviewScriptRepository
import com.idd.dia.application.dto.InterviewScriptRequest
import com.idd.dia.infra.web.InterviewQuestionScriptRestController
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType.NULL
import org.springframework.restdocs.payload.JsonFieldType.NUMBER
import org.springframework.restdocs.payload.JsonFieldType.STRING
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

/**
 * @see InterviewQuestionScriptRestController
 */
internal class InterviewQuestionScriptAcceptanceTest : AcceptanceTest() {

    @Autowired
    lateinit var interviewScriptRepository: InterviewScriptRepository

    @Autowired
    lateinit var interviewQuestionRepository: InterviewQuestionRepository

    @BeforeEach
    fun beforeTest() {
        interviewScriptRepository.deleteAll()
        interviewQuestionRepository.deleteAll()
    }

    @Test
    fun `스크립트 작성 - 유저는 면접 질문에 스크립트를 작성할 수 있다`() {
        val questionPk = interviewQuestionRepository.save(
            InterviewQuestion(userPk = null, title = "test")
        ).pk

        val scriptContent = "script content"
        val request = InterviewScriptRequest(scriptContent)

        val result = RestAssured.given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer $jwtToken")
            .body(jacksonObjectMapper().writeValueAsString(request))
            .filter(
                document(
                    "(post)interview-script",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("questionPk").description("문제 pk"),
                    ),
                    requestHeaders(
                        headerWithName("Authorization").description("JWT Bearer Token")
                    ),
                    requestFields(
                        fieldWithPath("content").type(STRING).description("스크립트 내용")
                    ),
                    responseFields(
                        fieldWithPath("data.pk").type(NUMBER).description("스크립트 pk"),
                        fieldWithPath("data.questionPk").type(NUMBER).description("문제 pk"),
                        fieldWithPath("data.content").type(STRING).description("스크립트 내용"),
                        fieldWithPath("data.createAt").type(STRING).description("생성 시각"),
                        fieldWithPath("data.updatedAt").type(STRING).description("업데이트 시각"),
                        fieldWithPath("message").type(NULL).description("알림 메세지"),
                    )
                )
            )
            .`when`().post("/api/v0/interview-questions/{questionPk}/interview-script", questionPk)
            .logAndReturn()

        with(result.jsonPath()) {
            getInt("data.questionPk") shouldBe questionPk
            getString("data.content") shouldBe scriptContent
        }
    }

    @Test
    fun `스크립트 단건 조회 - 유저는 작성한 스크립트를 볼 수 있다`() {
        val questionPk = interviewQuestionRepository.save(
            InterviewQuestion(userPk = null, title = "test")
        ).pk

        val scriptContent = "script content"
        val script = interviewScriptRepository.save(
            InterviewScript(userPk = loginUser.pk, questionPk = questionPk, content = scriptContent)
        )

        val result = RestAssured.given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer $jwtToken")
            .filter(
                document(
                    "(get)interview-script",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("questionPk").description("문제 pk"),
                    ),
                    requestHeaders(
                        headerWithName("Authorization").description("JWT Bearer Token")
                    ),
                    responseFields(
                        fieldWithPath("data.pk").type(NUMBER).description("스크립트 pk"),
                        fieldWithPath("data.questionPk").type(NUMBER).description("문제 pk"),
                        fieldWithPath("data.content").type(STRING).description("스크립트 내용"),
                        fieldWithPath("data.createAt").type(STRING).description("생성 시각"),
                        fieldWithPath("data.updatedAt").type(STRING).description("업데이트 시각"),
                        fieldWithPath("message").type(NULL).description("알림 메세지"),
                    )
                )
            )
            .get("/api/v0/interview-questions/{questionPk}/interview-script", questionPk)
            .logAndReturn()

        with(result.jsonPath()) {
            getInt("data.pk") shouldBe script.pk
            getInt("data.questionPk") shouldBe questionPk
            getString("data.content") shouldBe scriptContent
        }
    }
}
