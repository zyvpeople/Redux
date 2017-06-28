package com.develop.zuzik.redux.sample.readonlydata

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.asObserver
import com.develop.zuzik.redux.model.readonlydata.ReadOnlyData
import com.develop.zuzik.redux.model.readonlydata.ReadOnlyDataModel
import com.develop.zuzik.redux.model.readonlydata.ReadOnlyDataPresenter
import com.develop.zuzik.redux.sample.entity.User
import com.develop.zuzik.redux.sample.extension.showErrorToast
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v4.widget.refreshing
import com.jakewharton.rxbinding2.view.visibility
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_read_only_data.*

class ReadOnlyDataActivity : AppCompatActivity() {

	private val model: ReadOnlyData.Model<User> = ReadOnlyDataModel(User("", "", ""), UserQuery())
	private var presenter: ReadOnlyData.Presenter<User> = ReadOnlyDataPresenter(model)
	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_read_only_data)
		model.init()
	}

	override fun onDestroy() {
		model.release()
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()

		val view = object : ReadOnlyData.View<User> {
			override val displayProgress: Observer<in Boolean> = swipeRefreshLayout.refreshing().asObserver()
			override val displayData: PublishSubject<User> = PublishSubject.create()
			override val displayError: PublishSubject<Throwable> = PublishSubject.create()
			override val hideError: PublishSubject<Unit> = PublishSubject.create()
			override val displayErrorNotification: Observer<in Throwable> = showErrorToast().asObserver()
			override val onRefresh: Observable<Unit> = swipeRefreshLayout.refreshes()
		}

		intent(view
				.displayData
				.map(User::firstName)
				.distinctUntilChanged()
				.subscribe(etFirstName.text()))

		intent(view
				.displayData
				.map(User::lastName)
				.distinctUntilChanged()
				.subscribe(etLastName.text()))

		intent(view
				.displayData
				.map(User::email)
				.distinctUntilChanged()
				.subscribe(etEmail.text()))

		intent(view
				.displayError
				.map { it.toString() }
				.subscribe(tvError.text()))

		intent(Observable
				.merge(
						view
								.displayError
								.map { true },
						view
								.hideError
								.map { false })
				.subscribe(tvError.visibility()))

		presenter.onStart(view)
	}

	override fun onStop() {
		compositeDisposable.clear()
		presenter.onStop()
		super.onStop()
	}

	private fun intent(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}
}
