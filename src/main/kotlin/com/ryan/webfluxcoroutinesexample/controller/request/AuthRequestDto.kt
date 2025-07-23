package com.ryan.webfluxcoroutinesexample.controller.request

import com.ryan.webfluxcoroutinesexample.service.command.SignInCommand
import com.ryan.webfluxcoroutinesexample.service.command.SignUpCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignUpRequestDto(
    @field:NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @field:Email(message = "올바른 이메일 형식이어야 합니다.")
    @Schema(description = "이메일", example = "example@example.com")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @field:Size(min = 8, max = 16, message = "비밀번호는 최소 8자리에서 최대 16자리이어야 합니다.")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,16}$",
        message = "비밀번호는 대문자, 소문자, 숫자, 하나의 특수문자를 포함해야 합니다."
    )
    @Schema(description = "비밀번호", example = "1234qweR!!")
    val password: String
) {
    fun of(): SignUpCommand {
       return SignUpCommand(
            email = email,
            password = password
        )
    }
}

data class SignInRequestDto(
    @field:NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @field:Email(message = "올바른 이메일 형식이어야 합니다.")
    @Schema(description = "이메일", example = "example@example.com")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @field:Size(min = 8, max = 16, message = "비밀번호는 최소 8자리에서 최대 16자리이어야 합니다.")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,16}$",
        message = "비밀번호는 대문자, 소문자, 숫자, 하나의 특수문자를 포함해야 합니다."
    )
    @Schema(description = "비밀번호", example = "1234qweR!!")
    val password: String
) {
    fun of(): SignInCommand {
        return SignInCommand(
            email = email,
            password = password
        )
    }
}