package com.ryan.webfluxcoroutinesexample.controller

import com.ryan.webfluxcoroutinesexample.common.dto.ApiResponseDto
import com.ryan.webfluxcoroutinesexample.controller.request.SignInRequestDto
import com.ryan.webfluxcoroutinesexample.controller.request.SignUpRequestDto
import com.ryan.webfluxcoroutinesexample.controller.response.SignInResponseDto
import com.ryan.webfluxcoroutinesexample.controller.response.SignUpResponseDto
import com.ryan.webfluxcoroutinesexample.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증", description = "사용자 인증 관련 API")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/email/sign-up")
    @Operation(
        summary = "이메일 회원 가입",
        description = "이메일과 비밀번호를 통한 회원 가입",
        responses = [ApiResponse(
            responseCode = "200",
            description = "회원가입 성공",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = SignUpResponseDto::class
                    )
                )
            ]
        )]
    )
    suspend fun onSignUp(
        @RequestBody @Valid request: SignUpRequestDto
    ): ApiResponseDto<SignUpResponseDto> {
        val info = authService.onSignUp(request.of())
        return ApiResponseDto.setSuccess(SignUpResponseDto.of(info))
    }

    @PostMapping("/email/sign-in")
    @Operation(
        summary = "이메일 로그인",
        description = "이메일과 비밀번호를 통한 로그인",
        responses = [ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = SignInResponseDto::class
                    )
                )
            ]
        )]
    )
    suspend fun onSignIn(
        @RequestBody @Valid request: SignInRequestDto
    ): ApiResponseDto<SignInResponseDto> {
        val info = authService.onSignIn(request.of())
        return ApiResponseDto.setSuccess(SignInResponseDto.of(info))
    }
}