package com.develop.zuzik.redux.sample.editabledata

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.asObserver
import com.develop.zuzik.redux.model.editabledata.EditableData
import com.develop.zuzik.redux.model.editabledata.EditableDataModel
import com.develop.zuzik.redux.model.editabledata.EditableDataPresenter
import com.develop.zuzik.redux.sample.entity.User
import com.develop.zuzik.redux.sample.extension.showErrorToast
import com.develop.zuzik.redux.sample.extension.textIfNotEquals
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v4.widget.refreshing
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.enabled
import com.jakewharton.rxbinding2.view.visibility
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_editable_data.*

class EditableDataActivity : AppCompatActivity() {

	private val model: EditableData.Model<User> = EditableDataModel(User("", "", ""), UserQuery(), UpdateUserCommand())
	private var presenter: EditableData.Presenter<User> = EditableDataPresenter(model)
	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_editable_data)
		model.init()
	}

	override fun onDestroy() {
		model.release()
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()

		val view = object : EditableData.View<User> {
			override val displayProgress: Observer<in Boolean> = swipeRefreshLayout.refreshing().asObserver()
			override val displayData: PublishSubject<User> = PublishSubject.create()
			override val setDataEditable: PublishSubject<Boolean> = PublishSubject.create()
			override val allowRefresh: Observer<in Boolean> = swipeRefreshLayout.enabled().asObserver()
			override val allowEdit: Observer<in Boolean> = btnEdit.visibility().asObserver()
			override val allowCancelEdit: Observer<in Boolean> = btnCancelEdit.visibility().asObserver()
			override val allowSave: Observer<in Boolean> = btnSave.visibility().asObserver()
			override val displayError: PublishSubject<Throwable> = PublishSubject.create()
			override val hideError: PublishSubject<Unit> = PublishSubject.create()
			override val displayErrorNotification: Observer<in Throwable> = showErrorToast().asObserver()
			override val onRefresh: Observable<Unit> = swipeRefreshLayout.refreshes()
			override val onEdit: Observable<Unit> = btnEdit.clicks()
			override val onModify: Observable<User> = modifiedData()
			override val onCancelEdit: Observable<Unit> = btnCancelEdit.clicks()
			override val onSave: Observable<Unit> = btnSave.clicks()
		}


		intent(view
				.displayData
				.map(User::firstName)
				.distinctUntilChanged()
				.subscribe(etFirstName.textIfNotEquals()))

		intent(view
				.displayData
				.map(User::lastName)
				.distinctUntilChanged()
				.subscribe(etLastName.textIfNotEquals()))

		intent(view
				.displayData
				.map(User::email)
				.distinctUntilChanged()
				.subscribe(etEmail.textIfNotEquals()))

		intent(view
				.setDataEditable
				.subscribe(etFirstName.enabled()))

		intent(view
				.setDataEditable
				.subscribe(etLastName.enabled()))

		intent(view
				.setDataEditable
				.subscribe(etEmail.enabled()))

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

	private fun modifiedData(): Observable<User> =
			Observable.combineLatest(
					textChanges(etFirstName),
					textChanges(etLastName),
					textChanges(etEmail),
					Function3(::User))

	private fun textChanges(textView: TextView): Observable<String> =
			textView.textChanges().map { it.toString() }

}