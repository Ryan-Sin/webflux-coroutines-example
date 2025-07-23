package com.ryan.webfluxcoroutinesexample.service.command

data class AddFavoriteCommand(
    val email: String,
    val productsName: String,
    val drawerName: String
)

data class GetFavoriteListCommand(
    val email: String,
    val drawerName: String,
    val page: Int,
    val limit: Int
)

data class RemoveFromFavoriteCommand(
    val email: String,
    val favoriteId: Long
)