package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.ReduxModel
import com.develop.zuzik.redux.core.Version
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
//TODO: implement logic for next cases:
//TODO: check if current page not in pages (in constructor and in runtime)
//TODO: use tags
//TODO: in case of view pager do not listen state but push separate events
//TODO: page is active model (not data class) and needs init release methods etc
class PagesModel<Page>(pages: List<Page>, currentPage: Page) :
		ReduxModel<PagesState<Page>>(
				defaultState = PagesState(
						pages = Version(data = pages),
						currentPage = currentPage),
				modelScheduler = AndroidSchedulers.mainThread()),
		Pages.Model<Page> {

	override val dispatch: PublishSubject<PagesAction<Page>> = PublishSubject.create()

	init {
		addAction(dispatch.map { it })
		addReducer(PagesReducer())
	}
}