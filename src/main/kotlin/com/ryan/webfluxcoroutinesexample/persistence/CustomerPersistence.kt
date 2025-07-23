package com.ryan.webfluxcoroutinesexample.persistence

import com.ryan.webfluxcoroutinesexample.entity.CustomerEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface CustomerPersistence : R2dbcRepository<CustomerEntity, Long> {
}