package com.develop.zuzik.redux.model.entities

import io.reactivex.Observable

/**
 * User: zuzik
 * Date: 4/16/17
 */
interface EntitiesQuery<Entity, in Filter> {
	fun query(filter: Filter): Observable<List<Entity>>
}