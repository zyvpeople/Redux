package com.develop.zuzik.redux.model.entities

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.ErrorAction

/**
 * User: zuzik
 * Date: 4/16/17
 */
internal sealed class EntitiesAction<Entity, Filter> : Action {
	class BeginLoad<Entity, Filter> : EntitiesAction<Entity, Filter>()
	class Load<Entity, Filter>(val entities: List<Entity>, val filter: Filter) : EntitiesAction<Entity, Filter>()
	class HandleError<Entity, Filter>(override val error: Throwable) : EntitiesAction<Entity, Filter>(), ErrorAction
}