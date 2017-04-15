package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * User: zuzik
 * Date: 4/15/17
 */
internal class ReadOnlyLoadingModeEditableDataReducer<Data> : Reducer<EditableDataState<Data>> {

	override fun reduce(oldState: EditableDataState<Data>, action: Action): EditableDataState<Data> =
			(action as? EditableDataAction<Data>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: EditableDataState<Data>, action: EditableDataAction<Data>): EditableDataState<Data> =
			when (action) {
				is EditableDataAction.BeginLoading ->
					oldState.copy(
							loading = true)
				is EditableDataAction.Load ->
					oldState.copy(
							loading = false,
							error = null,
							originalData = action.data,
							editedData = action.data)
				is EditableDataAction.Edit ->
					oldState
				is EditableDataAction.CancelEdit ->
					oldState
				is EditableDataAction.ModifyEditedData ->
					oldState
				is EditableDataAction.BeginSaving ->
					oldState
				is EditableDataAction.Save ->
					oldState
				is EditableDataAction.HandleLoadingError ->
					oldState.copy(
							loading = false,
							error = action.error)
				is EditableDataAction.HandleSavingError ->
					oldState
			}
}