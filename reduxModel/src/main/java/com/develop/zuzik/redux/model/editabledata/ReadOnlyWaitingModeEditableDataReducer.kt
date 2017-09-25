package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Reducer

/**
 * User: zuzik
 * Date: 4/15/17
 */
internal class ReadOnlyWaitingModeEditableDataReducer<Data> : Reducer<EditableDataState<Data>> {

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
							editedData = oldState.editedData.newVersion(action.data))
				is EditableDataAction.Edit ->
					oldState.copy(
							editing = true,
							error = null)
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