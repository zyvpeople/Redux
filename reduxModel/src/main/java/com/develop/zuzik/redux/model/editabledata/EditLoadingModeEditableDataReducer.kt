package com.develop.zuzik.redux.model.editabledata

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * User: zuzik
 * Date: 4/15/17
 */
internal class EditLoadingModeEditableDataReducer<Data> : Reducer<EditableDataState<Data>> {

	override fun reduce(oldState: EditableDataState<Data>, action: Action): EditableDataState<Data> =
			(action as? EditableDataAction<Data>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: EditableDataState<Data>, action: EditableDataAction<Data>): EditableDataState<Data> =
			when (action) {
				is EditableDataAction.BeginLoading ->
					oldState
				is EditableDataAction.Load ->
					oldState.copy(
							originalData = action.data)
				is EditableDataAction.Edit ->
					oldState.copy(
							editing = true)
				is EditableDataAction.CancelEdit ->
					oldState
				is EditableDataAction.ModifyEditedData ->
					oldState.copy(
							editedData = action.editedData)
				is EditableDataAction.BeginSaving ->
					oldState.copy(
							loading = true)
				is EditableDataAction.Save ->
					oldState.copy(
							loading = false,
							error = null,
							editing = false,
							originalData = action.data,
							editedData = action.data)
				is EditableDataAction.HandleLoadingError ->
					oldState
				is EditableDataAction.HandleSavingError ->
					oldState.copy(
							loading = false,
							error = action.error)
			}
}