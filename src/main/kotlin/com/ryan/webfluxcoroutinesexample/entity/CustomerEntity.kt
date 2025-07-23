package com.ryan.webfluxcoroutinesexample.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "customer")
class CustomerEntity(
    @Id
    val id: Long? = null,

    @Column("email")
    val email: String,

    @Column("password")
    val password: String,

    @CreatedDate
    @Column("created_at")
    val createdAt: Instant? = null,

    @LastModifiedDate
    @Column("updated_at")
    var updatedAt: Instant? = null,

    @Column("deleted_at")
    val deletedAt: Instant? = null
)