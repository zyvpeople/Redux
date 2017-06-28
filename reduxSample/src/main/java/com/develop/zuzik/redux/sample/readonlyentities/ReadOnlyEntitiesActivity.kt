package com.develop.zuzik.redux.sample.readonlyentities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.asObserver
import com.develop.zuzik.redux.model.readonlyentities.ReadOnlyEntities
import com.develop.zuzik.redux.model.readonlyentities.ReadOnlyEntitiesModel
import com.develop.zuzik.redux.model.readonlyentities.ReadOnlyEntitiesPresenter
import com.develop.zuzik.redux.sample.entity.User
import com.develop.zuzik.redux.sample.extension.showErrorToast
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v4.widget.refreshing
import com.jakewharton.rxbinding2.view.visibility
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_entities.*
import java.util.concurrent.TimeUnit

class ReadOnlyEntitiesActivity : AppCompatActivity() {

	private val model: ReadOnlyEntities.Model<User, String> = ReadOnlyEntitiesModel("", UsersQuery())
	private val presenter: ReadOnlyEntities.Presenter<User, String> = ReadOnlyEntitiesPresenter(model)
	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_entities)
		model.init()
	}

	override fun onDestroy() {
		model.release()
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()

		val adapter = UserAdapter()
		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.adapter = adapter

		val displayUsers = Consumer<List<User>> {
			adapter.users = it
			adapter.notifyDataSetChanged()
		}

		val view = object : ReadOnlyEntities.View<User, String> {
			override val displayProgress: Observer<in Boolean> = swipeRefreshLayout.refreshing().asObserver()
			override val displayEntities: Observer<in List<User>> = displayUsers.asObserver()
			override val displayError: PublishSubject<Throwable> = PublishSubject.create()
			override val hideError: PublishSubject<Unit> = PublishSubject.create()
			override val displayErrorNotification: Observer<in Throwable> = showErrorToast().asObserver()
			override val onRefresh: Observable<Unit> = swipeRefreshLayout.refreshes()
			override val onFilter: Observable<String> = etFilter.textChanges().map(CharSequence::toString).debounce(1, TimeUnit.SECONDS)
		}

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

	private class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

		internal class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

			val tvName: TextView? = view.findViewById(R.id.tvName) as? TextView

			init {
				view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
			}
		}

		var users = emptyList<User>()

		override fun getItemCount(): Int = users.size

		override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserViewHolder =
				UserViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.view_user, null))

		override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
			holder?.tvName?.text = users[position].firstName
		}
	}
}