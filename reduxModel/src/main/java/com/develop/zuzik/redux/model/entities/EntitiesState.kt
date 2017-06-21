package com.develop.zuzik.redux.model.entities

import com.develop.zuzik.redux.core.Version

/**
 * User: zuzik
 * Date: 4/16/17
 */
data class EntitiesState<Entity, Filter>(val entities: Version<List<Entity>>,
										 val filter: Version<Filter>,
										 val loading: Boolean,
										 val error: Throwable?)