package com.develop.zuzik.redux.model.splash

import com.develop.zuzik.redux.core.Action

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
sealed class SplashAction : Action {
	class Hide : SplashAction()
}