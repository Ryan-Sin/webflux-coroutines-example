package com.ryan.webfluxcoroutinesexample.common.security

import com.ryan.webfluxcoroutinesexample.common.constant.SecurityInfo.PUBLIC_PATHS
import com.ryan.webfluxcoroutinesexample.common.constant.SecurityMethod
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint

@Configuration
@EnableWebFluxSecurity
class SpringSecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
    ): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange { exchanges: AuthorizeExchangeSpec ->
                PUBLIC_PATHS[SecurityMethod.GET]?.forEach { path ->
                    exchanges.pathMatchers(HttpMethod.GET, path).permitAll()
                }
                PUBLIC_PATHS[SecurityMethod.POST]?.forEach { path ->
                    exchanges.pathMatchers(HttpMethod.POST, path).permitAll()
                }
                PUBLIC_PATHS[SecurityMethod.PUT]?.forEach { path ->
                    exchanges.pathMatchers(HttpMethod.PUT, path).permitAll()
                }
                PUBLIC_PATHS[SecurityMethod.PATCH]?.forEach { path ->
                    exchanges.pathMatchers(HttpMethod.PATCH, path).permitAll()
                }
                PUBLIC_PATHS[SecurityMethod.DELETE]?.forEach { path ->
                    exchanges.pathMatchers(HttpMethod.DELETE, path).permitAll()
                }
                PUBLIC_PATHS[SecurityMethod.ALL]?.forEach { path ->
                    exchanges.pathMatchers(path).permitAll()
                }
                exchanges.anyExchange().authenticated()
            }
            .build()
    }

}