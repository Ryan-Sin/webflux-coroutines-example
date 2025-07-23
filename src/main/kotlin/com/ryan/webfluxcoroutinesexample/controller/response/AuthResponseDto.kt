package com.ryan.webfluxcoroutinesexample.controller.response

import com.ryan.webfluxcoroutinesexample.service.info.SignInInfo
import com.ryan.webfluxcoroutinesexample.service.info.SignUpInfo
import io.swagger.v3.oas.annotations.media.Schema

data class SignUpResponseDto(
    @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4YW1wbGVAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzUzMzUzMTMsImV4cCI6MjA0NjM3NTMxM30.VrvjOQLJoF_EVXr717UWeyarCYZFy4UHFgcxjSjeDBA")
    val accessToken: String,

    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4YW1wbGVAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzUzMzUzMTMsImV4cCI6MjA0NjM3NTMxM30.VrvjOQLJoF_EVXr717UWeyarCYZFy4UHFgcxjSjeDBA")
    val refreshToken: String
) {
    companion object {
        fun of(info: SignUpInfo): SignUpResponseDto {
            return SignUpResponseDto(
                accessToken = info.accessToken,
                refreshToken = info.refreshToken
            )
        }
    }
}

data class SignInResponseDto(
    @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4YW1wbGVAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzUzMzUzMTMsImV4cCI6MjA0NjM3NTMxM30.VrvjOQLJoF_EVXr717UWeyarCYZFy4UHFgcxjSjeDBA")
    val accessToken: String,

    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4YW1wbGVAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzUzMzUzMTMsImV4cCI6MjA0NjM3NTMxM30.VrvjOQLJoF_EVXr717UWeyarCYZFy4UHFgcxjSjeDBA")
    val refreshToken: String
) {
    companion object {
        fun of(info: SignInInfo): SignInResponseDto {
            return SignInResponseDto(
                accessToken = info.accessToken,
                refreshToken = info.refreshToken
            )
        }
    }
}