package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * User: zuzik
 * Date: 4/15/17
 */
internal class EditableDataReducer<Data> : Reducer<EditableDataState<Data>> {

	private val readOnlyWaitingModeReducer = ReadOnlyWaitingModeEditableDataReducer<Data>()
	private val readOnlyLoadingModeReducer = ReadOnlyLoadingModeEditableDataReducer<Data>()
	private val editWaitingModeReducer = EditWaitingModeEditableDataReducer<Data>()
	private val editLoadingModeReducer = EditLoadingModeEditableDataReducer<Data>()

	override fun reduce(oldState: EditableDataState<Data>, action: Action): EditableDataState<Data> =
			(action as? EditableDataAction<Data>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: EditableDataState<Data>, action: EditableDataAction<Data>): EditableDataState<Data> =
			if (oldState.editing)
				if (oldState.loading)
					editLoadingModeReducer.reduce(oldState, action)
				else
					editWaitingModeReducer.reduce(oldState, action)
			else
				if (oldState.loading)
					readOnlyLoadingModeReducer.reduce(oldState, action)
				else
					readOnlyWaitingModeReducer.reduce(oldState, action)
}