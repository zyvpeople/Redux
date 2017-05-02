package com.develop.zuzik.redux.sample.permissions

import android.content.ContextWrapper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.asConsumer
import com.develop.zuzik.redux.model.permissions.ActivityPermissionsView
import com.develop.zuzik.redux.model.permissions.Permissions
import com.develop.zuzik.redux.model.permissions.PermissionsModel
import com.develop.zuzik.redux.model.permissions.PermissionsPresenter
import com.develop.zuzik.redux.sample.extension.showErrorToast
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_permissions.*

class CounterActivity : AppCompatActivity() {

	private lateinit var permissionsModel: Permissions.Model
	private lateinit var permissionsPresenter: Permissions.Presenter
	private lateinit var permissionsView: ActivityPermissionsView
	private lateinit var testModel: TestModel
	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_permissions)

		permissionsModel = PermissionsModel(ContextWrapper(this).applicationContext)
		permissionsPresenter = PermissionsPresenter(permissionsModel)
		permissionsView = ActivityPermissionsView(this)
		testModel = TestModel(permissionsModel)

		permissionsModel.init()
		testModel.init()
	}

	override fun onDestroy() {
		permissionsModel.release()
		testModel.release()
		compositeDisposable.clear()
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()
		intent(testModel
				.state
				.map(TestState::counter)
				.map(Int::toString)
				.subscribe(counter.text()))
		intent(testModel
				.error
				.subscribe(showErrorToast()))
		intent(increment
				.clicks()
				.subscribe(testModel.increment.asConsumer()))
		intent(decrement
				.clicks()
				.subscribe(testModel.decrement.asConsumer()))

		permissionsPresenter.onStart(permissionsView)
	}

	override fun onStop() {
		permissionsPresenter.onStop()
		super.onStop()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		permissionsView.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	private fun intent(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}
}
