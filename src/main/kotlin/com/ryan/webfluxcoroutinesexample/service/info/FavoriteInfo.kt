package com.ryan.webfluxcoroutinesexample.service.info

data class GetFavoriteList(
    val hasNext: Boolean,
    val favoriteList: List<GetFavoriteInfo>
)

data class GetFavoriteInfo(
    val favoriteId: Long,
    val productsName: String,
    val thumbnail: String,
    val price: Long
)