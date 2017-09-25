package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.store.Action
import com.develop.zuzik.redux.core.model.value.Tag

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
sealed class PagesAction<Page> : Action {
	class AddPageToHead<Page>(val page: Tag<Page>) : PagesAction<Page>()
	class AddPageToTail<Page>(val page: Tag<Page>) : PagesAction<Page>()
	class AddPageAfterPage<Page>(val page: Tag<Page>, val existedPageTag: String) : PagesAction<Page>()
	class AddPageBeforePage<Page>(val page: Tag<Page>, val existedPageTag: String) : PagesAction<Page>()
	class RemovePage<Page>(val pageTag: String) : PagesAction<Page>()
	class NavigateToPage<Page>(val pageTag: String) : PagesAction<Page>()
	class NavigateBack<Page> : PagesAction<Page>()
	class NavigateForward<Page> : PagesAction<Page>()
	class CompositePageAction<Page>(val actions: List<PagesAction<Page>>) : PagesAction<Page>()
}