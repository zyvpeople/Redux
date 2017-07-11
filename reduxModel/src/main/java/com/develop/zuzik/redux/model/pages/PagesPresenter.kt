package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.ReduxPresenter
import com.develop.zuzik.redux.core.extension.UnitInstance

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class PagesPresenter<Page>(private val model: Pages.Model<Page>) :
		ReduxPresenter<Pages.View<Page>>(),
		Pages.Presenter<Page> {

	override fun onStart(view: Pages.View<Page>) {
		intent(model
				.versionProperty { it.pages }
				.subscribe(view.displayPages::onNext))
		intent(model
				.state
				.filter { it.currentPage != null }
				.map { it.currentPage!! }
				.subscribe(view.navigateToPage::onNext))

		intent(view
				.onNavigateToPage
				.map { PagesAction.NavigateToPage(it) }
				.subscribe(model.dispatch::onNext))
	}
}