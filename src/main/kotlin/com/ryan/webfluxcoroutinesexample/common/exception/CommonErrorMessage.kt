package com.ryan.webfluxcoroutinesexample.common.exception

object CommonErrorMessage {
    const val INTERNAL_SERVER_ERROR = "서버가 불안정합니다. 잠시 후 다시 시도해주세요."
    const val PAGE_NUMBER_CANNOT_BE_NEGATIVE = "페이지 번호는 음수 보다 큰 수여야 합니다."
    const val PAGE_SIZE_CANNOT_BE_NEGATIVE = "페이지 크기는 0 보다 큰 수여야 합니다."
    const val ORDER_BY_VALE = "정렬 기준 값은 ASC 또는 DESC 입니다."
    const val INVALID_TOKEN = "유효한 토큰이 아닙니다."
    const val NOT_EXIST_UNAUTHORIZED = "유효한 인증 자격 증명이 없습니다."
    const val EXIST_CUSTOMER = "고객이 존재합니다."
    const val NOT_EXIST_CUSTOMER = "고객이 존재하지 않습니다."
    const val PASSWORD_MISS_MATCH = "비밀번호가 틀렸습니다.";
    const val EXIST_DRAWER = "서랍이 존재합니다."
    const val EMPTY_DRAWER_LIST = "하나의 서랍도 존재하지 않았습니다."
    const val NOT_EXIST_DRAWER = "서랍이 존재하지 않습니다."
    const val NOT_EXIST_PRODUCT = "상품이 존재하지 않습니다."
    const val PRODUCT_ALREADY_IN_SAME_DRAWER = "해당 상품은 같은 서랍에 이미 존재합니다."
    const val PRODUCT_ALREADY_IN_OTHER_DRAWER = "해당 상품은 다른 서랍에 이미 존재합니다."
    const val NOT_EXIST_FAVORITE = "상품을 찜한 정보가 없습니다."
}