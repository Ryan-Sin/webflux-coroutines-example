package com.ryan.webfluxcoroutinesexample.common.security

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter(
	private val jwtUtil: JwtUtil,
) : WebFilter {

	private val logger = KotlinLogging.logger {}

	override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
		val request = exchange.request
		val authHeader = request.headers.getFirst("Authorization")
		
		logger.debug { "JWT Filter - Request URI: ${request.uri}" }
		logger.debug { "JWT Filter - Authorization header: $authHeader" }

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			val token = authHeader.substring(7)
			logger.debug { "JWT Filter - Token extracted: ${token.take(20)}..." }

			if (jwtUtil.validateToken(token)) {
				val username = jwtUtil.getUsernameFromToken(token)
				val userId = jwtUtil.getUserIdFromToken(token)
				val role = jwtUtil.getRoleFromToken(token)
				
				logger.debug { "JWT Filter - Token validation successful. Username: $username, UserId: $userId, Role: $role" }

				if (username != null && userId != null && role != null) {
					val authentication = UsernamePasswordAuthenticationToken(
						username,
						null,
						listOf(org.springframework.security.core.authority.SimpleGrantedAuthority(role.name))
					)
					authentication.details = userId

					logger.debug { "JWT Filter - Authentication object created and context written" }

					return chain.filter(exchange)
						.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
				}
			} else {
				logger.debug { "JWT Filter - Token validation failed" }
			}
		} else {
			logger.debug { "JWT Filter - No valid Authorization header found" }
		}

		return chain.filter(exchange)
	}
}
