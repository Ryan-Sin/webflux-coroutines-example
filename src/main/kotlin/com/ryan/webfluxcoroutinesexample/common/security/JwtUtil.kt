package com.ryan.webfluxcoroutinesexample.common.security

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

	private val logger = KotlinLogging.logger {}

	@Value("\${jwt.secret:defaultSecretKey}")
	private lateinit var secret: String

	@Value("\${jwt.expiration:3600000}")
	private var accessTokenExpiration: Long = 0

	@Value("\${jwt.refresh-expiration:2592000000}")
	private var refreshTokenExpiration: Long = 0

	private val key: SecretKey by lazy {
		val keyBytes = secret.toByteArray()
		val paddedKey = if (keyBytes.size < 64) {
			val padded = ByteArray(64)
			System.arraycopy(keyBytes, 0, padded, 0, minOf(keyBytes.size, 64))
			padded
		} else {
			keyBytes
		}
		javax.crypto.spec.SecretKeySpec(paddedKey, "HmacSHA512")
	}

	fun generateAccessToken(email: String, userId: Long, role: UserRole): String {
		val token = Jwts.builder()
			.subject(email)
			.claim("userId", userId)
			.claim("role", role.name)
			.expiration(Date.from(Instant.now().plus(accessTokenExpiration, ChronoUnit.HOURS)))
			.signWith(key, Jwts.SIG.HS512)
			.compact()

		logger.debug { "Access token 생성 완료: ${token.take(20)}..." }
		return token
	}

	fun generateRefreshToken(email: String, userId: Long, role: UserRole): String {
		val token = Jwts.builder()
			.subject(email)
			.claim("userId", userId)
			.claim("role", role.name)
			.expiration(Date.from(Instant.now().plus(refreshTokenExpiration, ChronoUnit.HOURS)))
			.signWith(key, Jwts.SIG.HS512)
			.compact()

		logger.debug { "Refresh token 생성 완료: ${token.take(20)}..." }
		return token
	}

	fun validateToken(token: String): Boolean {
		return try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
			true
		} catch (e: Exception) {
			false
		}
	}

	fun getUsernameFromToken(token: String): String? {
		return try {
			val claims = getClaimsFromToken(token)
			claims.subject
		} catch (e: Exception) {
			null
		}
	}

	fun getUserIdFromToken(token: String): String? {
		return try {
			val claims = getClaimsFromToken(token)
			claims["userId"] as? String
		} catch (e: Exception) {
			null
		}
	}

	fun getRoleFromToken(token: String): UserRole? {
		return try {
			val claims = getClaimsFromToken(token)
			val roleString = claims["role"] as? String
			roleString?.let { UserRole.valueOf(it) }
		} catch (e: Exception) {
			null
		}
	}

	private fun getClaimsFromToken(token: String): Claims {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.payload
	}
}
