package com.ryan.webfluxcoroutinesexample.service.validator

import com.ryan.webfluxcoroutinesexample.common.exception.CommonErrorMessage
import com.ryan.webfluxcoroutinesexample.common.exception.HttpCommonException
import com.ryan.webfluxcoroutinesexample.entity.ProductsEntity
import org.springframework.http.HttpStatus

fun validateProductsNotExists(products: ProductsEntity?): ProductsEntity {
    return products ?: throw HttpCommonException(HttpStatus.NOT_FOUND, CommonErrorMessage.NOT_EXIST_PRODUCT)
}