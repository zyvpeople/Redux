package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.Action

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
sealed class PagesAction<Page> : Action {
	class AddPageToHead<Page>(val page: Page) : PagesAction<Page>()
	class AddPageToTail<Page>(val page: Page) : PagesAction<Page>()
	class AddPageAfterPage<Page>(val page: Page, val existedPage: Page) : PagesAction<Page>()
	class AddPageBeforePage<Page>(val page: Page, val existedPage: Page) : PagesAction<Page>()
	class RemovePage<Page>(val page: Page) : PagesAction<Page>()
	class NavigateToPage<Page>(val page: Page) : PagesAction<Page>()
	class NavigateBack<Page> : PagesAction<Page>()
	class NavigateForward<Page> : PagesAction<Page>()
	class CompositePageAction<Page>(val actions: List<PagesAction<Page>>) : PagesAction<Page>()
	class SetPages<Page>(val pages: List<Page>) : PagesAction<Page>()
}