package com.ryan.webfluxcoroutinesexample.repository

import com.ryan.webfluxcoroutinesexample.common.extensions.active
import com.ryan.webfluxcoroutinesexample.entity.CustomerEntity
import com.ryan.webfluxcoroutinesexample.persistence.CustomerPersistence
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class CustomerRepository(
    private val customerPersistence: CustomerPersistence,
    private val entityTemplate: R2dbcEntityTemplate,
) {
    suspend fun save(
        email: String,
        password: String,
    ): CustomerEntity =
        customerPersistence.save(
            CustomerEntity(
                email = email,
                password = password
            )
        ).awaitSingle()

    suspend fun softDeleteById(id: Long) {
        entityTemplate.update(CustomerEntity::class.java)
            .matching(Query.query(Criteria.where("id").`is`(id).active()))
            .apply(Update.update("deleted_at", Instant.now()))
            .awaitFirstOrNull()
    }

    suspend fun findByEmail(email: String): CustomerEntity? {
        val query = Query.query(Criteria.where("email").`is`(email).active())
        return entityTemplate.select(query, CustomerEntity::class.java).awaitFirstOrNull()
    }
}