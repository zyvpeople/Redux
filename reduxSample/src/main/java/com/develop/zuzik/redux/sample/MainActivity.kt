package com.develop.zuzik.redux.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.sample.editabledata.EditableDataActivity
import com.develop.zuzik.redux.sample.entities.EntitiesActivity
import com.develop.zuzik.redux.sample.extension.startActivity
import com.develop.zuzik.redux.sample.permissions.ContactsActivity
import com.develop.zuzik.redux.sample.permissions.CounterActivity
import com.develop.zuzik.redux.sample.readonlydata.ReadOnlyDataActivity
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
		intent(btnEntities
				.clicks()
				.subscribe(startActivity(EntitiesActivity::class.java)))
		intent(btnCounter
				.clicks()
				.subscribe(startActivity(CounterActivity::class.java)))
		intent(btnContacts
				.clicks()
				.subscribe(startActivity(ContactsActivity::class.java)))
	}

	override fun onDestroy() {
		compositeDisposable.clear()
		super.onDestroy()
	}

	private fun intent(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}
}
