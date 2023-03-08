package com.idd.dia.acceptance

import com.idd.dia.application.domain.InterviewQuestion
import com.idd.dia.application.domain.InterviewQuestionRepository
import com.idd.dia.application.domain.User
import com.idd.dia.infra.security.JwtTokenProvider
import com.idd.dia.infra.web.InterviewQuestionRestController
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
        interviewQuestionRepository.save(
            InterviewQuestion(userPk = null, title = "test title", pk = 2L)
        )
    }

    @Test
    fun `유저에 귀속된 문제가 아니면 누구나 볼 수 있다`() {
        val user = User("jaeykweon")
        val jwt = jwtTokenProvider.createToken(user)

        val result = RestAssured.given(spec)
            .accept("application/json")
            .filter(
                document(
                    "interview-question",
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
            .`when`().get("/api/v0/interview-questions/1")
            .logAndReturn()

//        with(result.body.`as`(InterviewQuestionResponse::class.java)) {
//            pk shouldBe 1L
//            title shouldBe "test title"
//        }

        //.then().assertThat().statusCode(200)
    }
}
