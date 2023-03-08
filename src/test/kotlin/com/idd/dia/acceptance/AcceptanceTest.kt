package com.idd.dia.acceptance

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AcceptanceTest {

    @LocalServerPort
    private var port: Int = 0

    lateinit var spec: RequestSpecification

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port
        }
        this.spec = RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation)).build()
    }

    @AfterEach
    fun afterEach() {
    }

    protected fun Response.logAndReturn(): Response {
        this.then().log().all()
        return this
    }
}
