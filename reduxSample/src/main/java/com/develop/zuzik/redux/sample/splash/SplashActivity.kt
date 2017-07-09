package com.develop.zuzik.redux.sample.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.CompositeConsumer
import com.develop.zuzik.redux.core.extension.asObserver
import com.develop.zuzik.redux.model.splash.Splash
import com.develop.zuzik.redux.model.splash.SplashModel
import com.develop.zuzik.redux.model.splash.SplashPresenter
import com.develop.zuzik.redux.sample.extension.startActivity
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

	private val splashModel = SplashModel(3, TimeUnit.SECONDS)
	private val presenter: Splash.Presenter = SplashPresenter(splashModel)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		splashModel.init()
	}

	override fun onDestroy() {
		splashModel.release()
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()
		presenter.onStart(object : Splash.View {
			override val displaySplash: Observer<Unit> = PublishSubject.create()
			override val hideSplash: Observer<Unit> = CompositeConsumer(
					startActivity(NextAfterSplashActivity::class.java),
					Consumer { finish() }).asObserver()
		})
	}

	override fun onStop() {
		presenter.onStop()
		super.onStop()
	}
}
