package com.develop.zuzik.redux.model.readonlydata

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.ReduxModel
import com.develop.zuzik.redux.core.extension.UnitInstance
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * User: zuzik
 * Date: 4/15/17
 */
class ReadOnlyDataModel<Data>(
		defaultData: Data,
		private val dataQuery: DataQuery<Data>) :
		ReduxModel<ReadOnlyDataState<Data>>(ReadOnlyDataState(data = defaultData, loading = false, error = null)),
		ReadOnlyData.Model<Data> {

	override val refresh: PublishSubject<Unit> = PublishSubject.create<Unit>()

	init {
		val refreshFirstTime = Observable.just(UnitInstance.INSTANCE)
		addAction(
				Observable
						.merge(refreshFirstTime, refresh)
						.switchMap { refreshData() }
		)
		addReducer(ReadOnlyDataReducer())
	}

	private fun refreshData(): Observable<Action> =
			dataQuery
					.query()
					.map<Action> { ReadOnlyDataAction.Load(it) }
					.startWith(ReadOnlyDataAction.BeginLoad<Data>())
					.onErrorReturn { ReadOnlyDataAction.HandleError<Data>(it) }
}