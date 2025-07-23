package com.ryan.webfluxcoroutinesexample.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "favorite")
data class FavoriteEntity(
    @Id
    val id: Long? = null,

    val customer: CustomerEntity,

    val drawer: DrawerEntity,

    val products: ProductsEntity,

    @CreatedDate
    @Column("created_at")
    val createdAt: Instant = Instant.now()
)