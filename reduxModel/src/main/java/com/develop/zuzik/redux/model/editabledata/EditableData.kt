package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.model.Redux
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * User: zuzik
 * Date: 4/15/17
 */
interface EditableData {

	interface Model<Data> : Redux.Model<EditableDataState<Data>> {
		val refresh: Observer<Unit>
		val edit: Observer<Unit>
		val cancelEdit: Observer<Unit>
		val modify: Observer<Data>
		val save: Observer<Unit>
	}

	interface View<Data> : Redux.View {
		val displayProgress: Observer<in Boolean>
		val displayData: Observer<in Data>
		val setDataEditable: Observer<in Boolean>
		val allowRefresh: Observer<in Boolean>
		val allowEdit: Observer<in Boolean>
		val allowCancelEdit: Observer<in Boolean>
		val allowSave: Observer<in Boolean>
		val displayError: Observer<in Throwable>
		val hideError: Observer<in Unit>
		val displayErrorNotification: Observer<in Throwable>

		val onRefresh: Observable<Unit>
		val onEdit: Observable<Unit>
		val onCancelEdit: Observable<Unit>
		val onModify: Observable<Data>
		val onSave: Observable<Unit>
	}

	interface Presenter<Data> : Redux.Presenter<View<Data>>
}