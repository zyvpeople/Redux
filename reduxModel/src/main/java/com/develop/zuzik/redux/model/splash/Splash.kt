package com.develop.zuzik.redux.model.splash

import com.develop.zuzik.redux.core.model.Redux
import io.reactivex.Observer

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
interface Splash {

	interface Model : Redux.Model<SplashState>

	interface View : Redux.View {
		val displaySplash: Observer<Unit>
		val hideSplash: Observer<Unit>
	}

	interface Presenter : Redux.Presenter<View>
}