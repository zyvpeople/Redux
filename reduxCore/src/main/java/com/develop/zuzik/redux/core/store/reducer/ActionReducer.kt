package com.develop.zuzik.redux.core.store.reducer

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.store.Reducer

/**
 * Created by yaroslavzozulia on 9/26/17.
 */
abstract class ActionReducer<State, in A : Action>(private val actionClass: Class<A>) : Reducer<State> {

	protected abstract fun reduceAction(oldState: State, action: A): State

	override final fun reduce(oldState: State, action: Action): State =
			if(actionClass.isAssignableFrom(action::class.java)) reduceAction(oldState, action as A) else oldState
}