package com.develop.zuzik.redux.model.readonlyentities

import com.develop.zuzik.redux.core.Redux
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * User: zuzik
 * Date: 4/16/17
 */
interface ReadOnlyEntities {

	interface Model<Entity, Filter> : Redux.Model<ReadOnlyEntitiesState<Entity, Filter>> {
		val refresh: Observer<Unit>
		val filter: Observer<Filter>
	}

	interface View<in Entity, Filter> : Redux.View {
		val displayProgress: Observer<in Boolean>
		val displayEntities: Observer<in List<Entity>>
		val displayError: Observer<in Throwable>
		val hideError: Observer<in Unit>
		val displayErrorNotification: Observer<in Throwable>

		val onRefresh: Observable<Unit>
		val onFilter: Observable<Filter>
	}

	interface Presenter<Entity, Filter> : Redux.Presenter<View<Entity, Filter>>
}