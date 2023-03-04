package com.idd.dia.application.util

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.Signature
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Component
@Aspect
class LogAspect {

    private val logger = LoggerFactory.getLogger(LogAspect::class.java)

    @Before("execution(* com.idd.dia.infra.web.*.* (..))")
    fun logRequest(joinPoint: JoinPoint) {

        logger.info("joinPoint.args: ${joinPoint.args}")
    }

    // @Around("@annotation(LogExecutionTime)") // 클래스에 적용 시 동작하지 않음
    @Around("execution(* com.idd.dia.application.service.*.* (..))")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val stopWatch = StopWatch()

        stopWatch.start()
        val proceed = joinPoint.proceed()
        stopWatch.stop()

        logger.info("타겟 객체: ${joinPoint.target}")
        logger.info("실행 메서드: ${joinPoint.signature.getMethodName()}")
        logger.info("실행 시간: ${stopWatch.totalTimeMillis}ms")

        return proceed
    }

    private fun Signature.getMethodName(): String {
        return this.toString().split(".").last()
    }
}
