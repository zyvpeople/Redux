package com.develop.zuzik.redux.model.entities

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * User: zuzik
 * Date: 4/16/17
 */
internal class EntitiesReducer<Entity, Filter> : Reducer<EntitiesState<Entity, Filter>> {

	override fun reduce(oldState: EntitiesState<Entity, Filter>, action: Action): EntitiesState<Entity, Filter> =
			(action as? EntitiesAction<Entity, Filter>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: EntitiesState<Entity, Filter>, action: EntitiesAction<Entity, Filter>): EntitiesState<Entity, Filter> =
			when (action) {
				is EntitiesAction.BeginLoad ->
					oldState.copy(
							loading = true)
				is EntitiesAction.Load ->
					oldState.copy(
							loading = false,
							error = null,
							entities = action.entities,
							filter = action.filter)
				is EntitiesAction.HandleError ->
					oldState.copy(
							loading = false,
							error = action.error,
							entities = listOf())
			}
}