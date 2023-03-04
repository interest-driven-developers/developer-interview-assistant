package com.idd.dia.infra.web

data class ApiResponse<T>(
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T?, message: String? = null): ApiResponse<T> {
            return ApiResponse(data, message)
        }

        fun fail(message: String? = "정의된 에러 메세지가 없습니다."): ApiResponse<Unit> {
            return ApiResponse(data = null, message = message)
        }
    }
}
