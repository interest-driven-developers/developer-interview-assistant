package com.idd.dia.infra.security

class LoginFailedException(message: String = "올바르지 않은 로그인 정보입니다.") : RuntimeException(message)
