package com.develop.zuzik.redux.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.model.splash.Splash
import com.develop.zuzik.redux.model.splash.SplashAction
import com.develop.zuzik.redux.sample.editabledata.EditableDataActivity
import com.develop.zuzik.redux.sample.readonlyentities.ReadOnlyEntitiesActivity
import com.develop.zuzik.redux.sample.extension.startActivity
import com.develop.zuzik.redux.sample.lock.LockActivity
import com.develop.zuzik.redux.sample.operation.OperationActivity
import com.develop.zuzik.redux.sample.pages.PagesActivity
import com.develop.zuzik.redux.sample.permissions.PermissionsActivity
import com.develop.zuzik.redux.sample.readonlydata.ReadOnlyDataActivity
import com.develop.zuzik.redux.sample.splash.SplashActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		intent(btnReadOnlyData
				.clicks()
				.subscribe(startActivity(ReadOnlyDataActivity::class.java)))
		intent(btnEditableData
				.clicks()
				.subscribe(startActivity(EditableDataActivity::class.java)))
		intent(btnReadOnlyEntities
				.clicks()
				.subscribe(startActivity(ReadOnlyEntitiesActivity::class.java)))
		intent(btnPermissions
				.clicks()
				.subscribe(startActivity(PermissionsActivity::class.java)))
		intent(btnOperation
				.clicks()
				.subscribe(startActivity(OperationActivity::class.java)))
		intent(btnLock
				.clicks()
				.subscribe(startActivity(LockActivity::class.java)))
		intent(btnSplash
				.clicks()
				.subscribe(startActivity(SplashActivity::class.java)))
		intent(btnPages
				.clicks()
				.subscribe(startActivity(PagesActivity::class.java)))
	}

	override fun onDestroy() {
		compositeDisposable.clear()
		super.onDestroy()
	}

	private fun intent(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}
}
