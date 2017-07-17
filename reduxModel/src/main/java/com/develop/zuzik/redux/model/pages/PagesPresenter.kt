package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.ReduxPresenter

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
		//TODO: handle situation when pages exist but current page is null
		intent(model
				.state
				.filter { it.currentPageTag != null }
				.map { it.currentPageTag!! }
				.subscribe(view.navigateToPage::onNext))

		intent(view
				.onAddPageToHead
				.map { PagesAction.AddPageToHead(it) }
				.subscribe(model.dispatch::onNext))
		intent(view
				.onAddPageToTail
				.map { PagesAction.AddPageToTail(it) }
				.subscribe(model.dispatch::onNext))
		intent(view
				.onAddPageAfterPage
				.map { PagesAction.AddPageAfterPage(it.first, it.second) }
				.subscribe(model.dispatch::onNext))
		intent(view
				.onAddPageBeforePage
				.map { PagesAction.AddPageBeforePage(it.first, it.second) }
				.subscribe(model.dispatch::onNext))
		intent(view
				.onRemovePage
				.map { PagesAction.RemovePage<Page>(it) }
				.subscribe(model.dispatch::onNext))
		intent(view
				.onNavigateToPage
				.map { PagesAction.NavigateToPage<Page>(it) }
				.subscribe(model.dispatch::onNext))
		intent(view
				.onNavigateBack
				.map { PagesAction.NavigateBack<Page>() }
				.subscribe(model.dispatch::onNext))
		intent(view
				.onNavigateForward
				.map { PagesAction.NavigateForward<Page>() }
				.subscribe(model.dispatch::onNext))
	}
}