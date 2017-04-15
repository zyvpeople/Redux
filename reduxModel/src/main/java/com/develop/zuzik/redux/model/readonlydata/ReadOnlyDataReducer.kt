package com.develop.zuzik.redux.model.readonlydata

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * User: zuzik
 * Date: 4/15/17
 */
internal class ReadOnlyDataReducer<Data> : Reducer<ReadOnlyDataState<Data>> {

	override fun reduce(oldState: ReadOnlyDataState<Data>, action: Action): ReadOnlyDataState<Data> =
			(action as? ReadOnlyDataAction<Data>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: ReadOnlyDataState<Data>, action: ReadOnlyDataAction<Data>): ReadOnlyDataState<Data> =
			when (action) {
				is ReadOnlyDataAction.BeginLoading ->
					oldState.copy(
							loading = true)
				is ReadOnlyDataAction.Load ->
					oldState.copy(
							loading = false,
							data = action.data,
							error = null)
				is ReadOnlyDataAction.HandleError ->
					oldState.copy(
							loading = false,
							error = action.error)
			}
}