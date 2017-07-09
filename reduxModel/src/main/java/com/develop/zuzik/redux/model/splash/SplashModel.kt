package com.develop.zuzik.redux.model.splash

import com.develop.zuzik.redux.core.ReduxModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class SplashModel(delay: Long,
				  timeUnit: TimeUnit) :
		ReduxModel<SplashState>(
				defaultState = SplashState(displaySplash = true),
				modelScheduler = AndroidSchedulers.mainThread()),
		Splash.Model {

	init {
		addAction(
				Observable
						.timer(delay, timeUnit)
						.map { SplashAction.Hide() })
		addReducer(SplashReducer())
	}
}