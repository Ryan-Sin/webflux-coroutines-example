package com.ryan.webfluxcoroutinesexample.service.validator

import com.ryan.webfluxcoroutinesexample.common.exception.CommonErrorMessage
import com.ryan.webfluxcoroutinesexample.common.exception.HttpCommonException
import com.ryan.webfluxcoroutinesexample.entity.CustomerEntity
import org.springframework.http.HttpStatus

fun validateCustomerNotExists(customer: CustomerEntity?): CustomerEntity {
    return customer ?: throw HttpCommonException(HttpStatus.NOT_FOUND, CommonErrorMessage.NOT_EXIST_CUSTOMER)
}

fun validateCustomerExists(customer: CustomerEntity?) {
    if(customer != null) throw HttpCommonException(HttpStatus.CONFLICT, CommonErrorMessage.EXIST_CUSTOMER)
}