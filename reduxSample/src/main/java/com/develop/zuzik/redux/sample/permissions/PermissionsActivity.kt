package com.develop.zuzik.redux.sample.permissions

import android.content.Context
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
import com.develop.zuzik.redux.model.permissions.*
import com.develop.zuzik.redux.sample.extension.showErrorToast
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v4.widget.refreshing
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_permissions.*
import java.util.concurrent.TimeUnit

class PermissionsActivity : AppCompatActivity() {

	companion object Models {
		var permissionsModel: Permissions.Model? = null
		var entitiesModelContacts: Entities.Model<String, Unit>? = null
		var entitiesModelCalendar: Entities.Model<String, Unit>? = null
		var entitiesModelMicrophone: Entities.Model<String, Unit>? = null

		fun init(context: Context) {
			if (permissionsModel != null) {
				return
			}

			val applicationContext = ContextWrapper(context).applicationContext

			permissionsModel = PermissionsModel(applicationContext)
			entitiesModelContacts = EntitiesModel(UnitInstance.INSTANCE, EntitiesWithPermissionQuery(permissionsModel!!, "contacts", Permission.Contacts.ReadContacts()))
			entitiesModelCalendar = EntitiesModel(UnitInstance.INSTANCE, EntitiesWithPermissionQuery(permissionsModel!!, "calendar", Permission.Calendar.WriteCalendar()))
			entitiesModelMicrophone = EntitiesModel(UnitInstance.INSTANCE, EntitiesWithPermissionQuery(permissionsModel!!, "microphone", Permission.Microphone.RecordAudio()))

			permissionsModel!!.init()
			entitiesModelContacts!!.init()
			entitiesModelCalendar!!.init()
		}
	}

	private lateinit var permissionsPresenter: Permissions.Presenter
	private lateinit var permissionsView: ActivityPermissionsView

	private lateinit var entitiesPresenterContacts: Entities.Presenter<String, Unit>
	private lateinit var entitiesPresenterCalendar: Entities.Presenter<String, Unit>
	private lateinit var entitiesPresenterMicrophone: Entities.Presenter<String, Unit>

	private val compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_permissions)

		init(this)

		permissionsPresenter = PermissionsPresenter(permissionsModel!!)
		permissionsView = ActivityPermissionsView(this)


		entitiesPresenterContacts = EntitiesPresenter(entitiesModelContacts!!)
		entitiesPresenterCalendar = EntitiesPresenter(entitiesModelCalendar!!)
		entitiesPresenterMicrophone = EntitiesPresenter(entitiesModelMicrophone!!)

		entitiesModelMicrophone!!.init()

		intent(Observable
				.timer(10, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { entitiesModelMicrophone!!.release() })
	}

	override fun onDestroy() {
		entitiesModelMicrophone!!.release()
		compositeDisposable.clear()
		super.onDestroy()
	}

	override fun onStart() {
		super.onStart()

		permissionsPresenter.onStart(permissionsView)

		val adapter = EntitiesAdapter()
		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.adapter = adapter

		val displayContacts = Consumer<List<String>> {
			adapter.contacts = it
			adapter.notifyDataSetChanged()
		}

		val refresh = PublishSubject.create<Unit>()
		swipeRefreshLayout.refreshes().subscribe { refresh.onNext(it) }

		val view = object : Entities.View<String, Unit> {
			override val displayProgress: Observer<in Boolean> = swipeRefreshLayout.refreshing().asObserver()
			override val displayEntities: Observer<in List<String>> = displayContacts.asObserver()
			override val displayError: Observer<Throwable> = PublishSubject.create()
			override val hideError: Observer<Unit> = PublishSubject.create()
			override val displayErrorNotification: Observer<in Throwable> = showErrorToast().asObserver()
			override val onRefresh: Observable<Unit> = refresh
			override val onFilter: Observable<Unit> = Observable.never()
		}

		entitiesPresenterContacts.onStart(view)
		entitiesPresenterCalendar.onStart(view)
		entitiesPresenterMicrophone.onStart(view)
	}

	override fun onStop() {
		permissionsPresenter.onStop()
		entitiesPresenterContacts.onStop()
		entitiesPresenterCalendar.onStop()
		entitiesPresenterMicrophone.onStop()
		super.onStop()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		permissionsView.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	private fun intent(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}

	private class EntitiesAdapter : RecyclerView.Adapter<EntitiesAdapter.ContactViewHolder>() {

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