package com.develop.zuzik.redux.model.entities

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.ReduxModel
import com.develop.zuzik.redux.core.Version
import com.develop.zuzik.redux.core.extension.UnitInstance
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * User: zuzik
 * Date: 4/16/17
 */
class EntitiesModel<Entity, Filter>(defaultFilter: Filter,
									private val entitiesQuery: EntitiesQuery<Entity, Filter>) :
		ReduxModel<EntitiesState<Entity, Filter>>(EntitiesState(Version(data = listOf()), Version(data = defaultFilter), false, null)),
		Entities.Model<Entity, Filter> {

	override val refresh: PublishSubject<Unit> = PublishSubject.create()
	override val filter: BehaviorSubject<Filter> = BehaviorSubject.createDefault(defaultFilter)

	init {
		addAction(Observable
				.combineLatest(
						refresh.startWith(UnitInstance.INSTANCE),
						filter,
						BiFunction<Unit, Filter, Filter> { refresh, filter -> filter })
				.switchMap { loadEntities(it) })
		addReducer(EntitiesReducer())
	}

	private fun loadEntities(filter: Filter): Observable<Action> =
			entitiesQuery
					.query(filter)
					.map<Action> { EntitiesAction.Load(it, filter) }
					.startWith(EntitiesAction.BeginLoad<Entity, Filter>())
					.onErrorReturn { EntitiesAction.HandleError<Entity, Filter>(it) }
}