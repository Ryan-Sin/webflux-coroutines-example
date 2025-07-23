package com.ryan.webfluxcoroutinesexample.service.info

data class SignUpInfo(
    val accessToken: String,
    val refreshToken: String
)

data class SignInInfo(
    val accessToken: String,
    val refreshToken: String
)