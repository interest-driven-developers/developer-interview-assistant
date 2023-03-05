package com.idd.dia.application.util

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig(
    private val connectTimeOut: Int = 3000,
    private val readTimeOut: Int = 3000
) {

    @Bean
    fun restTemplate(): RestTemplate {
        val httpRequestFactory = HttpComponentsClientHttpRequestFactory().apply {
            setConnectTimeout(connectTimeOut)
            setReadTimeout(readTimeOut)
        }
        return RestTemplate(httpRequestFactory)
    }
}
