package com.develop.zuzik.redux.model.splash

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class SplashReducer : Reducer<SplashState> {

	override fun reduce(oldState: SplashState, action: Action): SplashState =
			(action as? SplashAction)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: SplashState, action: SplashAction): SplashState =
			when (action) {
				is SplashAction.Hide -> oldState.copy(displaySplash = false)
			}
}