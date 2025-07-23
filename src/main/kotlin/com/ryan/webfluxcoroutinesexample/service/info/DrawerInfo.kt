package com.ryan.webfluxcoroutinesexample.service.info

data class RegisterDrawerInfo(
    val id: Long,
    val name: String,
    val thumbnail: String,
)

data class GetDrawerInfoList(
    val hasNext: Boolean,
    val drawerList: List<GetDrawerInfo>
)

data class GetDrawerInfo(
    val id: Long,
    val name: String,
    val thumbnail: String
)