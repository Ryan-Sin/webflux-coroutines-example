package com.ryan.webfluxcoroutinesexample.service

import com.ryan.webfluxcoroutinesexample.common.exception.CommonErrorMessage
import com.ryan.webfluxcoroutinesexample.common.exception.HttpCommonException
import com.ryan.webfluxcoroutinesexample.common.security.JwtUtil
import com.ryan.webfluxcoroutinesexample.common.security.UserRole
import com.ryan.webfluxcoroutinesexample.repository.CustomerRepository
import com.ryan.webfluxcoroutinesexample.service.command.SignInCommand
import com.ryan.webfluxcoroutinesexample.service.command.SignUpCommand
import com.ryan.webfluxcoroutinesexample.service.info.SignInInfo
import com.ryan.webfluxcoroutinesexample.service.info.SignUpInfo
import com.ryan.webfluxcoroutinesexample.service.validator.validateCustomerExists
import com.ryan.webfluxcoroutinesexample.service.validator.validateCustomerNotExists
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil
) {

    suspend fun onSignUp(command: SignUpCommand): SignUpInfo {
        validateCustomerExists(customerRepository.findByEmail(command.email))
        val customer = customerRepository.save(command.email, passwordEncoder.encode(command.password))
        val accessToken = jwtUtil.generateAccessToken(customer.email, customer.id!!, UserRole.CUSTOMER)
        val refreshToken = jwtUtil.generateRefreshToken(customer.email, customer.id, UserRole.CUSTOMER)
        return SignUpInfo(accessToken, refreshToken)
    }

    suspend fun onSignIn(command: SignInCommand): SignInInfo {
        val customer = validateCustomerNotExists(customerRepository.findByEmail(command.email))
        validatePasswordMissMatches(command.password, customer.password)
        val accessToken = jwtUtil.generateAccessToken(customer.email, customer.id!!, UserRole.CUSTOMER)
        val refreshToken = jwtUtil.generateRefreshToken(customer.email, customer.id, UserRole.CUSTOMER)
        return SignInInfo(accessToken, refreshToken)
    }

    suspend fun reIssueRefreshToken() {

    }

    private fun validatePasswordMissMatches(newPassword: String, oldPassword: String) {
        if(!this.passwordEncoder.matches(newPassword, oldPassword))
            throw HttpCommonException(HttpStatus.BAD_REQUEST, CommonErrorMessage.PASSWORD_MISS_MATCH)
    }
}