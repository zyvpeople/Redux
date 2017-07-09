package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.Redux
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Created by yaroslavzozulia on 7/9/17.
 */

interface Pages {

	interface Model<Page> : Redux.Model<PagesState<Page>> {
		val navigateToPage: Observer<Page>
	}

	interface View<Page> : Redux.View {
		val displayPages: Observer<List<Page>>
		val navigateToPage: Observer<Page>

		val onNavigateToPage: Observable<Page>
	}

	interface Presenter<Page> : Redux.Presenter<View<Page>>
}