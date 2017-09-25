package com.develop.zuzik.redux.core.model

import com.develop.zuzik.redux.core.model.value.Version
import io.reactivex.Observable

/**
 * User: zuzik
 * Date: 4/15/17
 */
interface Redux {

	interface Model<State> {

		val state: Observable<State>
		val error: Observable<Throwable>

		fun init()
		fun release()

		fun <Property> property(propertySelector: (State) -> Property): Observable<Property>
		fun <Property> versionProperty(versionPropertySelector: (State) -> Version<Property>): Observable<Property>
	}

	interface View

	interface Presenter<in V : View> {
		fun onStart(view: V)
		fun onStop()
	}
}