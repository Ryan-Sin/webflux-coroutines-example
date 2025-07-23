package com.ryan.webfluxcoroutinesexample.service.command

data class SignUpCommand(
    val email: String,
    val password: String
)

data class SignInCommand(
    val email: String,
    val password: String
)