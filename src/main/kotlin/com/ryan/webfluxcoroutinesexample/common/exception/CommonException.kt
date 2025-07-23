package com.ryan.webfluxcoroutinesexample.common.exception

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
data class HttpCommonException(
    val status: HttpStatus = HttpStatus.OK,
    val clientErrorMessage: String? = null,
    val serverErrorMessage: String? = null
) : RuntimeException()
