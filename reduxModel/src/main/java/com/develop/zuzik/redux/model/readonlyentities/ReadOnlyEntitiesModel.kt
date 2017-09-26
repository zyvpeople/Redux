package com.develop.zuzik.redux.model.readonlyentities

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.model.ReduxModel
import com.develop.zuzik.redux.core.model.Version
import com.develop.zuzik.redux.core.extension.UnitInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * User: zuzik
 * Date: 4/16/17
 */
class ReadOnlyEntitiesModel<Entity, Filter>(defaultFilter: Filter,
											private val entitiesQuery: ReadOnlyEntitiesQuery<Entity, Filter>) :
		ReduxModel<ReadOnlyEntitiesState<Entity, Filter>>(
				ReadOnlyEntitiesState(Version(data = listOf()), Version(data = defaultFilter), false, null),
				AndroidSchedulers.mainThread()),
		ReadOnlyEntities.Model<Entity, Filter> {

	override val refresh: PublishSubject<Unit> = PublishSubject.create()
	override val filter: BehaviorSubject<Filter> = BehaviorSubject.createDefault(defaultFilter)

	init {
		addAction(Observable
				.combineLatest(
						refresh.startWith(UnitInstance.INSTANCE),
						filter,
						BiFunction<Unit, Filter, Filter> { refresh, filter -> filter })
				.switchMap { loadEntities(it) })
		addReducer(ReadOnlyEntitiesReducer())
	}

	private fun loadEntities(filter: Filter): Observable<Action> =
			entitiesQuery
					.query(filter)
					.map<Action> { ReadOnlyEntitiesAction.Load(it, filter) }
					.startWith(ReadOnlyEntitiesAction.BeginLoad<Entity, Filter>())
					.onErrorReturn { ReadOnlyEntitiesAction.HandleError<Entity, Filter>(it) }
}