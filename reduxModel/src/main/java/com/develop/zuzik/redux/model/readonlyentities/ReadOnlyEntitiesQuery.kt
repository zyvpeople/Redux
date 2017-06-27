package com.develop.zuzik.redux.model.readonlyentities

import io.reactivex.Observable

/**
 * User: zuzik
 * Date: 4/16/17
 */
interface ReadOnlyEntitiesQuery<Entity, in Filter> {
	fun query(filter: Filter): Observable<List<Entity>>
}