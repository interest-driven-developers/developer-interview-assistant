package com.idd.dia.acceptance

import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.application.domain.InterviewScript
import com.idd.dia.application.domain.InterviewScriptRepository
import com.idd.dia.infra.web.InterviewScriptRestController
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document

/**
 * @see InterviewScriptRestController
 */
internal class InterviewScriptAcceptanceTest : AcceptanceTest() {

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
    fun `스크립트 리스트 조회 - 유저는 작성한 스크립트 리스트를 볼 수 있다`() {
        val question1 = interviewQuestionRepository.save(
            InterviewQuestion(userPk = null, title = "test")
        )
        val question2 = interviewQuestionRepository.save(
            InterviewQuestion(userPk = null, title = "test2")
        )

        val scriptContent1 = "script content1"
        val scriptContent2 = "script content2"
        val script1 = InterviewScript(userPk = loginUser.pk, questionPk = question1.pk, content = scriptContent1)
        val script2 = InterviewScript(userPk = loginUser.pk, questionPk = question2.pk, content = scriptContent2)

        interviewScriptRepository.saveAll(listOf(script1, script2))

        val result = RestAssured.given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Bearer $jwtToken")
            .filter(
                document(
                    "(get)interview-scripts",
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("page").description("page"),
                        parameterWithName("size").description("size")
                    ),
                    requestHeaders(
                        headerWithName("Authorization").description("JWT Bearer Token")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("data.content[].pk").type(JsonFieldType.NUMBER).description("스크립트 pk"),
                        fieldWithPath("data.content[].questionPk").type(JsonFieldType.NUMBER).description("문제 pk"),
                        fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("스크립트 내용"),
                        fieldWithPath("data.content[].createAt").type(JsonFieldType.STRING).description("생성 시각"),
                        fieldWithPath("data.content[].updatedAt").type(JsonFieldType.STRING).description("업데이트 시각"),
                        fieldWithPath("message").type(JsonFieldType.NULL).description("알림 메세지"),
                    )
                )
            )
            .get("/api/v0/interview-scripts?page=0&size=10")
            .logAndReturn()

        with(result.jsonPath()) {
            getInt("data.content[0].pk") shouldBe script1.pk
            getInt("data.content[0].questionPk") shouldBe question1.pk
            getString("data.content[0].content") shouldBe scriptContent1
            getInt("data.content[1].pk") shouldBe script2.pk
            getInt("data.content[1].questionPk") shouldBe question2.pk
            getString("data.content[1].content") shouldBe scriptContent2
        }
    }
}
