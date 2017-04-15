package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.ReduxModel
import com.develop.zuzik.redux.core.extension.UnitInstance
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * User: zuzik
 * Date: 4/15/17
 */
class EditableDataModel<Data>(defaultData: Data,
							  private val dataQuery: DataQuery<Data>,
							  private val updateDataCommand: UpdateDataCommand<Data>) :
		ReduxModel<EditableDataState<Data>>(EditableDataState(originalData = defaultData, editedData = defaultData, loading = false, editing = false, error = null)),
		EditableData.Model<Data> {

	override val refresh: PublishSubject<Unit> = PublishSubject.create()
	override val edit: PublishSubject<Unit> = PublishSubject.create()
	override val cancelEdit: PublishSubject<Unit> = PublishSubject.create()
	override val modify: PublishSubject<Data> = PublishSubject.create()
	override val save: PublishSubject<Unit> = PublishSubject.create()

	init {
		val refreshFirstTime = Observable.just(UnitInstance.INSTANCE)
		addAction(
				Observable
						.merge(refreshFirstTime, refresh)
						.switchMap { refreshData() })
		addAction(edit.map { EditableDataAction.Edit<Data>() })
		addAction(cancelEdit.map { EditableDataAction.CancelEdit<Data>() })
		addAction(modify.map { EditableDataAction.ModifyEditedData<Data>(it) })
		addAction(save.switchMap { saveData() })

		addReducer(EditableDataReducer())
	}

	private fun refreshData(): Observable<Action> =
			dataQuery
					.query()
					.map<Action> { EditableDataAction.Load(it) }
					.startWith(EditableDataAction.BeginLoading<Data>())
					.onErrorReturn { EditableDataAction.HandleLoadingError<Data>(it) }

	private fun saveData(): Observable<Action> =
			state
					.take(1)
					.map { it.editedData }
					.flatMap { updateDataCommand.update(it).toObservable() }
					.map<Action> { EditableDataAction.Save(it) }
					.startWith(EditableDataAction.BeginSaving<Data>())
					.onErrorReturn { EditableDataAction.HandleSavingError<Data>(it) }
}