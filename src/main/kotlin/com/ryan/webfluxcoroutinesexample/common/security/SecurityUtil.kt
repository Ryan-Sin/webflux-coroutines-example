package com.ryan.webfluxcoroutinesexample.common.security

import com.ryan.webfluxcoroutinesexample.common.exception.HttpCommonException
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class SecurityUtil {

    fun getCurrentUserId(): Mono<UUID> {
        return ReactiveSecurityContextHolder.getContext()
            .handle { context, sink ->
                val authentication = context.authentication
                if (authentication != null && authentication.details != null) {
                    sink.next(UUID.fromString(authentication.details.toString()))
                } else {
                    sink.error(HttpCommonException(HttpStatus.UNAUTHORIZED, "인증된 사용자 정보를 찾을 수 없습니다"))
                }
            }
    }

    fun getCurrentUserRole(): Mono<UserRole> {
        return ReactiveSecurityContextHolder.getContext()
            .map { context: SecurityContext ->
                val authentication = context.authentication
                if (authentication != null && authentication.authorities.isNotEmpty()) {
                    val roleString = authentication.authorities.first().authority
                    UserRole.valueOf(roleString)
                } else {
                    UserRole.CUSTOMER
                }
            }
    }

    fun getCurrentUserIdAndRole(): Mono<Pair<UUID, UserRole>> {
        return ReactiveSecurityContextHolder.getContext()
            .handle { context, sink ->
                val authentication = context.authentication
                if (authentication != null && authentication.details != null) {
                    val userId = UUID.fromString(authentication.details.toString())
                    val role = if (authentication.authorities.isNotEmpty()) {
                        val roleString = authentication.authorities.first().authority
                        UserRole.valueOf(roleString)
                    } else {
                        UserRole.CUSTOMER
                    }
                    sink.next(Pair(userId, role))
                } else {
                    sink.error(HttpCommonException(HttpStatus.UNAUTHORIZED, "인증된 사용자 정보를 찾을 수 없습니다"))
                }
            }
    }
} 