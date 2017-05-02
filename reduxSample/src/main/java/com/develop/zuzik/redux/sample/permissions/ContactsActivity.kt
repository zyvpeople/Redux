package com.develop.zuzik.redux.sample.permissions

import android.content.ContextWrapper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.UnitInstance
import com.develop.zuzik.redux.core.extension.asObserver
import com.develop.zuzik.redux.model.entities.Entities
import com.develop.zuzik.redux.model.entities.EntitiesModel
import com.develop.zuzik.redux.model.entities.EntitiesPresenter
import com.develop.zuzik.redux.model.permissions.ActivityPermissionsView
import com.develop.zuzik.redux.model.permissions.Permissions
import com.develop.zuzik.redux.model.permissions.PermissionsModel
import com.develop.zuzik.redux.model.permissions.PermissionsPresenter
import com.develop.zuzik.redux.sample.extension.showErrorToast
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v4.widget.refreshing
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity() {

	private lateinit var permissionsModel: Permissions.Model
	private lateinit var permissionsPresenter: Permissions.Presenter
	private lateinit var permissionsView: ActivityPermissionsView
	private lateinit var entitiesModel: Entities.Model<String, Unit>
	private lateinit var entitiesPresenter: Entities.Presenter<String, Unit>
	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_contacts)

		permissionsModel = PermissionsModel(ContextWrapper(this).applicationContext)
		permissionsPresenter = PermissionsPresenter(permissionsModel)
		permissionsView = ActivityPermissionsView(this)

		entitiesModel = EntitiesModel(UnitInstance.INSTANCE, ContactsQuery(this, permissionsModel))
		entitiesPresenter = EntitiesPresenter(entitiesModel)

		permissionsModel.init()
		entitiesModel.init()
	}

	override fun onDestroy() {
		permissionsModel.release()
		entitiesModel.release()
		compositeDisposable.clear()
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()

		permissionsPresenter.onStart(permissionsView)

		val adapter = ContactAdapter()
		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.adapter = adapter

		val displayContacts = Consumer<List<String>> {
			adapter.contacts = it
			adapter.notifyDataSetChanged()
		}

		val view = object : Entities.View<String, Unit> {
			override val displayProgress: Observer<in Boolean> = swipeRefreshLayout.refreshing().asObserver()
			override val displayEntities: Observer<in List<String>> = displayContacts.asObserver()
			override val displayError: Observer<Throwable> = PublishSubject.create()
			override val hideError: Observer<Unit> = PublishSubject.create()
			override val displayErrorNotification: Observer<in Throwable> = showErrorToast().asObserver()
			override val onRefresh: Observable<Unit> = swipeRefreshLayout.refreshes()
			override val onFilter: Observable<Unit> = Observable.never()
		}

		entitiesPresenter.onStart(view)
	}

	override fun onStop() {
		permissionsPresenter.onStop()
		entitiesPresenter.onStop()
		super.onStop()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		permissionsView.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	private fun intent(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}

	private class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

		internal class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {

			val tvName: TextView? = view.findViewById(R.id.tvName) as? TextView

			init {
				view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
			}
		}

		var contacts = emptyList<String>()

		override fun getItemCount(): Int = contacts.size

		override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContactViewHolder =
				ContactViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.view_contact, null))

		override fun onBindViewHolder(holder: ContactViewHolder?, position: Int) {
			holder?.tvName?.text = contacts[position]
		}
	}
}