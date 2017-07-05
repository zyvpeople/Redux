package com.develop.zuzik.redux.model.lock

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * Created by yaroslavzozulia on 7/4/17.
 */
internal class LockReducer : Reducer<LockState> {

	override fun reduce(oldState: LockState, action: Action): LockState =
			(action as? LockAction)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: LockState, action: LockAction): LockState =
			when (action) {
				is LockAction.Lock -> oldState.copy(locked = true)
				is LockAction.Unlock -> oldState.copy(locked = false)
			}
}