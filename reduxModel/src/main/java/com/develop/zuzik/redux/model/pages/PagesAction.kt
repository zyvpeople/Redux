package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.Action

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
sealed class PagesAction<Page> : Action {
	class NavigateToPage<Page>(val page: Page) : PagesAction<Page>()
	class AddPage<Page>(val page: Page, val position: Int) : PagesAction<Page>()
	class RemovePage<Page>(val page: Page) : PagesAction<Page>()
	class CompositePageAction<Page>(val actions: List<PagesAction<Page>>) : PagesAction<Page>()
}