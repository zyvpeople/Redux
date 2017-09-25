package com.develop.zuzik.redux.model.splash

import com.develop.zuzik.redux.core.model.ReduxPresenter
import com.develop.zuzik.redux.core.extension.UnitInstance

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class SplashPresenter(private val model: Splash.Model) :
		ReduxPresenter<Splash.View>(),
		Splash.Presenter {

	override fun onStart(view: Splash.View) {
		intent(model
				.state
				.map { it.displaySplash }
				.filter { it }
				.map { UnitInstance.INSTANCE }
				.subscribe(view.displaySplash::onNext))
		intent(model
				.state
				.map { it.displaySplash }
				.filter { !it }
				.map { UnitInstance.INSTANCE }
				.subscribe(view.hideSplash::onNext))
	}
}