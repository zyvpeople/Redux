package com.develop.zuzik.redux.model.entities

/**
 * User: zuzik
 * Date: 4/16/17
 */
data class EntitiesState<out Entity, out Filter>(val entities: List<Entity>,
												 val filter: Filter,
												 val loading: Boolean,
												 val error: Throwable?)