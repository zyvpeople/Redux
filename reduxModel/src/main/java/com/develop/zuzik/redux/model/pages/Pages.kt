package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.Redux
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Created by yaroslavzozulia on 7/9/17.
 */

interface Pages {

	interface Model<Page> : Redux.Model<PagesState<Page>> {
		val dispatch: Observer<PagesAction<Page>>
	}

	interface View<Page> : Redux.View {
		val displayPages: Observer<List<Page>>
		val navigateToPage: Observer<Page>

		val onAddPageToHead: Observable<Page>
		val onAddPageToTail: Observable<Page>
		val onAddPageAfterPage: Observable<Pair<Page, Page>>
		val onAddPageBeforePage: Observable<Pair<Page, Page>>
		val onRemovePage: Observable<Page>
		val onNavigateToPage: Observable<Page>
		val onNavigateBack: Observable<Unit>
		val onNavigateForward: Observable<Unit>
		val onSetPages: Observable<List<Page>>
	}

	interface Presenter<Page> : Redux.Presenter<View<Page>>
}