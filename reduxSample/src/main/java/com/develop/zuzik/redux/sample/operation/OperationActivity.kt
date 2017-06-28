package com.develop.zuzik.redux.sample.operation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.asObserver
import com.develop.zuzik.redux.model.operation.Operation
import com.develop.zuzik.redux.model.operation.OperationModel
import com.develop.zuzik.redux.model.operation.OperationPresenter
import com.develop.zuzik.redux.sample.extension.showToast
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.visibility
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_operation.*

class OperationActivity : AppCompatActivity() {

	companion object Model {

		val uploadAlbumOperationModel: Operation.Model<String, Progress> by lazy {
			val model = OperationModel<String, Progress>(UploadAlbumOperationCommand())
			model.init()
			model
		}
	}

	private val presenter: Operation.Presenter<String, Progress> = OperationPresenter(uploadAlbumOperationModel)
	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_operation)
	}

	override fun onStart() {
		super.onStart()

		val view = object : Operation.View<String, Progress> {
			override val displayProgress: PublishSubject<Pair<String, Progress>> = PublishSubject.create()
			override val hideProgress: PublishSubject<Unit> = PublishSubject.create()
			override val displaySuccess: PublishSubject<String> = PublishSubject.create()
			override val hideSuccess: PublishSubject<Unit> = PublishSubject.create()
			override val displayError: PublishSubject<Pair<String, Throwable>> = PublishSubject.create()
			override val hideError: PublishSubject<Unit> = PublishSubject.create()
			override val displayCanceled: PublishSubject<String> = PublishSubject.create()
			override val hideCanceled: PublishSubject<Unit> = PublishSubject.create()
			override val displaySuccessNotification: Observer<String> = showToast().asObserver()
			override val displayErrorNotification: PublishSubject<Pair<String, Throwable>> = PublishSubject.create()
			override val displayCanceledNotification: Observer<String> = showToast().asObserver()
			override val onExecute: Observable<String> = btnRun
					.clicks()
					.map { etAlbumName.text.toString() }
			override val onReset: Observable<Unit> = btnCancel.clicks()

		}

		intent(Observable
				.merge(
						view.displayProgress.map { true },
						view.hideProgress.map { false })
				.subscribe(progressContainer.visibility(View.INVISIBLE)))
		intent(view
				.displayProgress
				.map { "${it.second.current}/${it.second.total}" }
				.subscribe(tvProgress.text()))

		intent(Observable
				.merge(
						view.displaySuccess.map { true },
						view.hideSuccess.map { false })
				.subscribe(tvSuccess.visibility()))
		intent(view
				.displaySuccess
				.map { "Album '$it' uploaded" }
				.subscribe(tvSuccess.text()))

		intent(Observable
				.merge(
						view.displayError.map { true },
						view.hideError.map { false })
				.subscribe(tvError.visibility()))
		intent(view
				.displayError
				.map { "Error upload album '${it.first}' - ${it.second}" }
				.subscribe(tvError.text()))

		intent(Observable
				.merge(
						view.displayCanceled.map { true },
						view.hideCanceled.map { false })
				.subscribe(tvCanceled.visibility()))
		intent(view
				.displayCanceled
				.map { "Uploading of album '$it' is canceled" }
				.subscribe(tvCanceled.text()))

		intent(view
				.displayErrorNotification
				.map { "Error upload album '${it.first}' - ${it.second}" }
				.subscribe(showToast()))

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
