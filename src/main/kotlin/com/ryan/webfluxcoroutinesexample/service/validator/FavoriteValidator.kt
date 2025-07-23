package com.ryan.webfluxcoroutinesexample.service.validator

import com.ryan.webfluxcoroutinesexample.common.exception.CommonErrorMessage
import com.ryan.webfluxcoroutinesexample.common.exception.HttpCommonException
import com.ryan.webfluxcoroutinesexample.entity.FavoriteEntity
import org.springframework.http.HttpStatus
import java.util.*

fun validateFavoriteNotExists(favorite: Optional<FavoriteEntity>): FavoriteEntity {
   return favorite.orElseThrow {
       HttpCommonException(HttpStatus.BAD_REQUEST, CommonErrorMessage.NOT_EXIST_FAVORITE)
   }
}

fun validateDrawerProductsExists(favoriteList: List<FavoriteEntity>, productsName: String, drawerName: String) {
    favoriteList.find { it.products.name == productsName }?.let { favorite ->
        val errorMessage = if (favorite.drawer.name == drawerName) {
            CommonErrorMessage.PRODUCT_ALREADY_IN_SAME_DRAWER
        } else {
            CommonErrorMessage.PRODUCT_ALREADY_IN_OTHER_DRAWER
        }
        throw HttpCommonException(HttpStatus.BAD_REQUEST, errorMessage)
    }
}
