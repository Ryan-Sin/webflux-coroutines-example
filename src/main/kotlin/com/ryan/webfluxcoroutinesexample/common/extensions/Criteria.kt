package com.ryan.webfluxcoroutinesexample.common.extensions

import org.springframework.data.relational.core.query.Criteria

fun Criteria.active(): Criteria = this.and("deleted_at").isNull