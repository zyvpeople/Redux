package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer
import com.develop.zuzik.redux.core.Tag

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
//TODO: add logic to init/release start/stop page when add/remove navigate
//TODO: check by tag or reference or equality
//TODO: add strategy for history when navigate
//TODO: add strategy for navigation when remove
class PagesReducer<Page> : Reducer<PagesState<Page>> {

	override fun reduce(oldState: PagesState<Page>, action: Action): PagesState<Page> =
			(action as? PagesAction<Page>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: PagesState<Page>, action: PagesAction<Page>): PagesState<Page> =
			when (action) {
				is PagesAction.AddPageToHead -> reduceAddPageToHead(oldState, action)
				is PagesAction.AddPageToTail -> reduceAddPageToTail(oldState, action)
				is PagesAction.AddPageAfterPage -> reduceAddPageAfterPage(oldState, action)
				is PagesAction.AddPageBeforePage -> reduceAddPageBeforePage(oldState, action)
				is PagesAction.RemovePage -> reduceRemovePage(oldState, action)
				is PagesAction.NavigateToPage -> reduceNavigateToPage(oldState, action)
				is PagesAction.NavigateBack -> reduceNavigateBack(oldState, action)
				is PagesAction.NavigateForward -> reduceNavigateForward(oldState, action)
				is PagesAction.CompositePageAction -> reduceCompositePageAction(oldState, action)
				is PagesAction.SetPages -> reduceSetPages(oldState, action)
			}

	private fun reduceAddPageToHead(oldState: PagesState<Page>, action: PagesAction.AddPageToHead<Page>): PagesState<Page> =
			if (pageExists(oldState, action.page)) {
				oldState
			} else {
				val pagesWithNewHead = oldState.pages.data.toMutableList().apply { add(0, action.page) }.toList()
				oldState.copy(pages = oldState.pages.newVersion(pagesWithNewHead))
			}

	private fun reduceAddPageToTail(oldState: PagesState<Page>, action: PagesAction.AddPageToTail<Page>): PagesState<Page> =
			if (pageExists(oldState, action.page)) {
				oldState
			} else {
				val pagesWithNewTail = oldState.pages.data + action.page
				oldState.copy(pages = oldState.pages.newVersion(pagesWithNewTail))
			}

	private fun reduceAddPageAfterPage(oldState: PagesState<Page>, action: PagesAction.AddPageAfterPage<Page>): PagesState<Page> =
			if (!pageExists(oldState, action.existedPageTag)
					|| pageExists(oldState, action.page)) {
				oldState
			} else {
				val existedPagePosition = pageIndex(oldState, action.existedPageTag)
				val pagesWithInsertedPage = oldState.pages.data.toMutableList().apply { add(existedPagePosition + 1, action.page) }.toList()
				oldState.copy(pages = oldState.pages.newVersion(pagesWithInsertedPage))
			}

	private fun reduceAddPageBeforePage(oldState: PagesState<Page>, action: PagesAction.AddPageBeforePage<Page>): PagesState<Page> =
			if (!pageExists(oldState, action.existedPageTag)
					|| pageExists(oldState, action.page)) {
				oldState
			} else {
				val existedPagePosition = pageIndex(oldState, action.existedPageTag)
				val pagesWithInsertedPage = oldState.pages.data.toMutableList().apply { add(existedPagePosition, action.page) }.toList()
				oldState.copy(pages = oldState.pages.newVersion(pagesWithInsertedPage))
			}

	private fun reduceRemovePage(oldState: PagesState<Page>, action: PagesAction.RemovePage<Page>): PagesState<Page> =
			if (!pageExists(oldState, action.pageTag)) {
				oldState
			} else {
				val pagePosition = pageIndex(oldState, action.pageTag)
				val pagesWithRemovedPage = oldState.pages.data.toMutableList().apply { removeAt(pagePosition) }.toList()
				val clearCurrentPage = action.pageTag == oldState.currentPageTag
				oldState.copy(
						pages = oldState.pages.newVersion(pagesWithRemovedPage),
						currentPageTag = if (clearCurrentPage) null else oldState.currentPageTag)
			}

	private fun reduceNavigateToPage(oldState: PagesState<Page>, action: PagesAction.NavigateToPage<Page>): PagesState<Page> =
			if (!pageExists(oldState, action.pageTag)) {
				oldState
			} else {
				oldState.copy(currentPageTag = action.pageTag)
			}

	private fun reduceNavigateBack(oldState: PagesState<Page>, action: PagesAction.NavigateBack<Page>): PagesState<Page> =
			if (oldState.currentPageTag == null) {
				oldState
			} else {
				val currentPageIndex = pageIndex(oldState, oldState.currentPageTag)
				if (currentPageIndex > 0) {
					oldState.copy(currentPageTag = oldState.pages.data[currentPageIndex - 1].tag)
				} else {
					oldState
				}
			}

	private fun reduceNavigateForward(oldState: PagesState<Page>, action: PagesAction.NavigateForward<Page>): PagesState<Page> =
			if (oldState.currentPageTag == null) {
				oldState
			} else {
				val currentPageIndex = pageIndex(oldState, oldState.currentPageTag)
				if ((0 until (oldState.pages.data.size - 1)).contains(currentPageIndex)) {
					oldState.copy(currentPageTag = oldState.pages.data[currentPageIndex + 1].tag)
				} else {
					oldState
				}
			}

	private fun reduceCompositePageAction(oldState: PagesState<Page>, action: PagesAction.CompositePageAction<Page>): PagesState<Page> =
			action
					.actions
					.fold(oldState, this::reduce)

	private fun reduceSetPages(oldState: PagesState<Page>, action: PagesAction.SetPages<Page>): PagesState<Page> =
			oldState.copy(
					pages = oldState.pages.newVersion(action.pages),
					currentPageTag = null)

	private fun pageExists(state: PagesState<Page>, page: Tag<Page>): Boolean =
			pageExists(state, page.tag)

	private fun pageExists(state: PagesState<Page>, pageTag: String): Boolean =
			pageIndex(state, pageTag) != -1

	private fun pageIndex(state: PagesState<Page>, page: Tag<String>): Int =
			pageIndex(state, page.tag)

	private fun pageIndex(state: PagesState<Page>, pageTag: String): Int =
			state.pages.data.indexOfFirst { it.tag == pageTag }
}