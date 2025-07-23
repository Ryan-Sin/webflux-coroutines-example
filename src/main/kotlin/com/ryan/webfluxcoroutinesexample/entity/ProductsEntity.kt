package com.ryan.webfluxcoroutinesexample.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "products")
class ProductsEntity(
    @Id
    var id: Long? = null,

    @Column("name")
    val name: String,

    @Column("thumbnail")
    val thumbnail: String,

    @Column("price")
    val price: Long
)