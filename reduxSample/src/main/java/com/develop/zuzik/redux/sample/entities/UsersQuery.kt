package com.develop.zuzik.redux.sample.entities

import com.develop.zuzik.redux.model.entities.EntitiesQuery
import com.develop.zuzik.redux.sample.entity.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * User: zuzik
 * Date: 4/16/17
 */
class UsersQuery : EntitiesQuery<User, String> {

	private var counter = 0

	override fun query(filter: String): Observable<List<User>> =
			when ((counter++) % 3) {
				2 -> Observable.error(RuntimeException("Error loading users"))
				else -> users()
						.filter { it.firstName.contains(filter, true) }
						.toList()
						.delay(2, TimeUnit.SECONDS)
						.toObservable()
			}


	private fun users(): Observable<User> =
			Observable.fromArray(
					User("ABCD", "ABCD", "ABCD"),
					User("BCDE", "BCDE", "BCDE"),
					User("CDEF", "CDEF", "CDEF"),
					User("DEFG", "DEFG", "DEFG"),
					User("EFGH", "EFGH", "EFGH"),
					User("FGHI", "FGHI", "FGHI"),
					User("GHIJ", "GHIJ", "GHIJ"),
					User("HIJK", "HIJK", "HIJK"),
					User("IJKL", "IJKL", "IJKL"),
					User("JKLM", "JKLM", "JKLM"),
					User("KLMN", "KLMN", "KLMN"),
					User("LMNO", "LMNO", "LMNO"),
					User("MNOP", "MNOP", "MNOP"))
}