package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.model.ReduxModel
import com.develop.zuzik.redux.core.model.value.Tag
import com.develop.zuzik.redux.core.model.value.Version
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class PagesModel<Page>(pages: List<Tag<Page>>,
					   currentPageTag: String,
					   pageInteractionStrategy: PageInteractionStrategy<Page>) :
		ReduxModel<PagesState<Page>>(
				defaultState = PagesState(
						pages = Version(data = pages),
						currentPageTag = currentPageTag),
				modelScheduler = AndroidSchedulers.mainThread()),
		Pages.Model<Page> {

	override val dispatch: PublishSubject<PagesAction<Page>> = PublishSubject.create()

	init {
		val initState = Observable
				.fromIterable(
						pages.map { PagesAction.AddPageToTail(it) } + PagesAction.NavigateToPage<Page>(currentPageTag))
		addAction(
				initState
						.concatWith(dispatch)
						.map { it })
		addReducer(PagesReducer(pageInteractionStrategy))
	}
}