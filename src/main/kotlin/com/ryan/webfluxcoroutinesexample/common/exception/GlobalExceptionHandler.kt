package com.ryan.webfluxcoroutinesexample.common.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.ryan.webfluxcoroutinesexample.common.dto.ErrorResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

import java.time.Instant

@RestControllerAdvice
@ConditionalOnWebApplication(type= ConditionalOnWebApplication.Type.REACTIVE)
class GlobalExceptionHandler {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        exception: Exception,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponseDto>> {
        val path = exchange.request.path.toString()
        log.error("Exception [Path]: {} [Message]: {}", path, exception)
        return Mono.just(
            ResponseEntity(
                ErrorResponseDto(
                    timeStamp = Instant.now().toString(),
                    message = "Internal server error"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        )
    }

    @ExceptionHandler(HttpCommonException::class)
    fun handleCommonException(
        httpCommonException: HttpCommonException,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponseDto>> {
        val path = exchange.request.path.toString()
        log.debug(
            "CommonException [Path]: {} [Status]: {} [ClientErrorMessage]: {} [ServerErrorMessage]: {}",
            path,
            httpCommonException.status,
            httpCommonException.clientErrorMessage,
            httpCommonException.serverErrorMessage
        )
        return Mono.just(
            ResponseEntity(
                ErrorResponseDto(
                    timeStamp = Instant.now().toString(),
                    message = httpCommonException.clientErrorMessage
                ),
                httpCommonException.status
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        exception: MethodArgumentNotValidException,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponseDto>> {
        val errorMessages = exception.bindingResult.allErrors.map { error: ObjectError ->
            if (error is FieldError) {
                "${error.field}: ${error.defaultMessage}"
            } else {
                error.defaultMessage ?: ""
            }
        }
        val path = exchange.request.path.toString()
        log.error("MethodArgumentNotValidException [Path]: {} [Message]: {}", path, errorMessages)
        return Mono.just(
            ResponseEntity(
                ErrorResponseDto(
                    timeStamp = Instant.now().toString(),
                    message = errorMessages
                ),
                HttpStatus.BAD_REQUEST
            )
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        exception: HttpMessageNotReadableException,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponseDto>> {
        val cause = exception.cause
        val errorMessage = when (cause) {
            is InvalidFormatException -> {
                "${cause.path.firstOrNull()?.fieldName ?: "필드"}: 올바른 형식이 아닙니다"
            }
            is MismatchedInputException -> {
                "${cause.path.firstOrNull()?.fieldName ?: "필드"}: 필수 필드입니다"
            }
            else -> "요청 형식이 올바르지 않습니다"
        }
        val path = exchange.request.path.toString()
        log.error("HttpMessageNotReadableException [Path]: {} [Message]: {}", path, errorMessage)

        return Mono.just(
            ResponseEntity(
                ErrorResponseDto(
                    timeStamp = Instant.now().toString(),
                    message = listOf(errorMessage)
                ),
                HttpStatus.BAD_REQUEST
            )
        )
    }

}
