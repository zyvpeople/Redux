package com.develop.zuzik.redux.sample.permissions

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.develop.zuzik.redux.model.entities.EntitiesQuery
import com.develop.zuzik.redux.model.permissions.Permission
import com.develop.zuzik.redux.model.permissions.Permissions
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * User: zuzik
 * Date: 5/2/17
 */
class ContactsQuery(
		private val context: Context,
		private val permissionsModel: Permissions.Model) : EntitiesQuery<String, Unit> {

	override fun query(filter: Unit): Observable<List<String>> {
		val projection = arrayOf(
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME)

		return Observable
				.just(context.contentResolver)
				.observeOn(Schedulers.io())
				.flatMap {
					Observable
							.using({ it.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null) },
									{
										Observable
												.just(it)
												.flatMap {
													var contacts = emptyList<String>()
													it.moveToFirst()
													while (!it.isAfterLast) {
														contacts += it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
														it.moveToNext()
													}
													Observable.just(contacts)
												}
									},
									Cursor::close)
				}
				.compose(permissionsModel.checkPermission(Permission.Contacts.ReadContacts()))
	}

}