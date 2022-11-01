package com.kotlinspring.exception.handler

import com.kotlinspring.exception.InstructorNotValidException
import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import kotlin.Exception

@Component
@ControllerAdvice
class GlobalExceptionHandler: ResponseEntityExceptionHandler() {
    companion object: KLogging()

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("MethodArgumentNotValidException observed: ${ex.message}", ex)

        val errors = ex.bindingResult.allErrors
            .map { it.defaultMessage!! }
            .sorted()

        logger.info("errors: $errors")

        return ResponseEntity.status(status).body(errors.joinToString(", "))
    }

    @ExceptionHandler(InstructorNotValidException::class)
    fun handleInstructorNotValidExceptions(ex: InstructorNotValidException, request: WebRequest) : ResponseEntity<Any> {
        logger.error("RuntimeException observed: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest) : ResponseEntity<Any> {
        logger.error("RuntimeException observed: ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }
}