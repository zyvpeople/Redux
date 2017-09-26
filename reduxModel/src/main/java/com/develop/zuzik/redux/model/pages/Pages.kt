package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.model.Redux
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
		val displayPages: Observer<List<Tag<Page>>>
		val navigateToPage: Observer<String>

		val onAddPageToHead: Observable<Tag<Page>>
		val onAddPageToTail: Observable<Tag<Page>>
		val onAddPageAfterPage: Observable<AddPageData<Page>>
		val onAddPageBeforePage: Observable<AddPageData<Page>>
		val onRemovePage: Observable<String>
		val onNavigateToPage: Observable<String>
		val onNavigateBack: Observable<Unit>
		val onNavigateForward: Observable<Unit>
	}

	interface Presenter<Page> : Redux.Presenter<View<Page>>
}