package com.ryan.webfluxcoroutinesexample.service.command

data class RegisterDrawerCommand(
    val email: String,
    val name: String,
    val thumbnail: String
)

data class GetDrawerListCommand(
    val email: String,
    val page: Int,
    val limit: Int
)

data class RemoveDrawerCommand(
    val email: String,
    val drawerId: Long,
)