package com.ryan.webfluxcoroutinesexample.service.validator

import com.ryan.webfluxcoroutinesexample.common.exception.CommonErrorMessage
import com.ryan.webfluxcoroutinesexample.common.exception.HttpCommonException
import com.ryan.webfluxcoroutinesexample.entity.DrawerEntity
import org.springframework.http.HttpStatus

fun validateDrawerNotExists(drawer: DrawerEntity?): DrawerEntity {
    return drawer ?: throw HttpCommonException(HttpStatus.NOT_FOUND, CommonErrorMessage.NOT_EXIST_DRAWER)
}

fun validateDrawerExists(drawer: DrawerEntity?) {
    if (drawer != null) throw HttpCommonException(HttpStatus.CONFLICT, CommonErrorMessage.EXIST_DRAWER)
}

fun validateDrawerNotExists(drawerList: List<DrawerEntity>): List<DrawerEntity> {
    return drawerList.takeIf { it.isEmpty() }
        ?.let { throw HttpCommonException(HttpStatus.NOT_FOUND, CommonErrorMessage.EMPTY_DRAWER_LIST) }
        ?: drawerList
}

fun validateDrawerNotExists(drawerList: List<DrawerEntity>, drawerName:String): DrawerEntity {
    return drawerList.find { it.name == drawerName }?: throw HttpCommonException(HttpStatus.NOT_FOUND, CommonErrorMessage.NOT_EXIST_DRAWER)
}