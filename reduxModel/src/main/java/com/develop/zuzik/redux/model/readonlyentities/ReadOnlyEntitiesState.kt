package com.develop.zuzik.redux.model.readonlyentities

import com.develop.zuzik.redux.core.model.value.Version

/**
 * User: zuzik
 * Date: 4/16/17
 */
data class ReadOnlyEntitiesState<Entity, Filter>(val entities: Version<List<Entity>>,
												 val filter: Version<Filter>,
												 val loading: Boolean,
												 val error: Throwable?)