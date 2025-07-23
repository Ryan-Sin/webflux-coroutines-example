package com.ryan.webfluxcoroutinesexample.common.constant

enum class SecurityMethod {
    ALL, GET, POST, PUT, PATCH, DELETE
}

object SecurityInfo {
    val PUBLIC_PATHS: Map<SecurityMethod, List<String>> = mapOf(
        SecurityMethod.GET to listOf(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs/swagger-config",
            "/api-docs/**",
            "/health",
        ),
        SecurityMethod.POST to listOf(),
        SecurityMethod.PUT to listOf(),
        SecurityMethod.ALL to listOf(
            "/api/auth/*/**"
        )
    )
}