package com.develop.zuzik.redux.model.readonlyentities

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.ErrorAction

/**
 * User: zuzik
 * Date: 4/16/17
 */
internal sealed class ReadOnlyEntitiesAction<Entity, Filter> : Action {
	class BeginLoad<Entity, Filter> : ReadOnlyEntitiesAction<Entity, Filter>()
	class Load<Entity, Filter>(val entities: List<Entity>, val filter: Filter) : ReadOnlyEntitiesAction<Entity, Filter>()
	class HandleError<Entity, Filter>(override val error: Throwable) : ReadOnlyEntitiesAction<Entity, Filter>(), ErrorAction
}