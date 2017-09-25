package com.develop.zuzik.redux.model.readonlyentities

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Reducer

/**
 * User: zuzik
 * Date: 4/16/17
 */
internal class ReadOnlyEntitiesReducer<Entity, Filter> : Reducer<ReadOnlyEntitiesState<Entity, Filter>> {

	override fun reduce(oldState: ReadOnlyEntitiesState<Entity, Filter>, action: Action): ReadOnlyEntitiesState<Entity, Filter> =
			(action as? ReadOnlyEntitiesAction<Entity, Filter>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: ReadOnlyEntitiesState<Entity, Filter>, action: ReadOnlyEntitiesAction<Entity, Filter>): ReadOnlyEntitiesState<Entity, Filter> =
			when (action) {
				is ReadOnlyEntitiesAction.BeginLoad ->
					oldState.copy(
							loading = true)
				is ReadOnlyEntitiesAction.Load ->
					oldState.copy(
							loading = false,
							error = null,
							entities = oldState.entities.newVersion(action.entities),
							filter = oldState.filter.newVersion(action.filter))
				is ReadOnlyEntitiesAction.HandleError ->
					oldState.copy(
							loading = false,
							error = action.error,
							entities = oldState.entities.newVersion(listOf()))
			}
}