package com.develop.zuzik.redux.sample.application

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.model.application.Application
import com.develop.zuzik.redux.model.application.ApplicationModel
import com.develop.zuzik.redux.model.application.ApplicationState
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_application.*

class ApplicationActivity : AppCompatActivity() {

	companion object {
		val applicationModel = ApplicationModel(DatabaseApplicationStrategy()).apply {
			init()
		}
	}

	private var disposable: Disposable? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_application)
	}

	override fun onStart() {
		super.onStart()
		disposable = applicationModel
				.state
				.map(this::applicationStateToText)
				.subscribe(tvApplicationStatus.text())
	}

	override fun onStop() {
		disposable?.dispose()
		super.onStop()
	}

	private fun applicationStateToText(state: ApplicationState<Database>) =
			when (state) {
				is ApplicationState.Initializing -> "Initializing (wait until important modules will be initialized)"
				is ApplicationState.Initialized -> "Initialized (continue your domain logic here). ${state.data}"
				is ApplicationState.Error -> "Error (important modules is not initialized. Close application and notify user about it). ${state.error}"
			}
}
