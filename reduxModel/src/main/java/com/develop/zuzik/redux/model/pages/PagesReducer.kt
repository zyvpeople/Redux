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
class PagesReducer<Page>(private val pageInteractionStrategy: PageInteractionStrategy<Page>) : Reducer<PagesState<Page>> {

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
				action
						.page
						.updateData(pageInteractionStrategy::onAdded)
						.let { oldState.addPageToPosition(it, 0) }
			}

	private fun reduceAddPageToTail(oldState: PagesState<Page>, action: PagesAction.AddPageToTail<Page>): PagesState<Page> =
			if (pageExists(oldState, action.page)) {
				oldState
			} else {
				action
						.page
						.updateData(pageInteractionStrategy::onAdded)
						.let { oldState.addPageToPosition(it, oldState.pages.data.size) }
			}

	private fun reduceAddPageAfterPage(oldState: PagesState<Page>, action: PagesAction.AddPageAfterPage<Page>): PagesState<Page> =
			if (!pageExists(oldState, action.existedPageTag)
					|| pageExists(oldState, action.page)) {
				oldState
			} else {
				action
						.page
						.updateData(pageInteractionStrategy::onAdded)
						.let { oldState.addPageToPosition(it, pageIndex(oldState, action.existedPageTag) + 1) }
			}

	private fun reduceAddPageBeforePage(oldState: PagesState<Page>, action: PagesAction.AddPageBeforePage<Page>): PagesState<Page> =
			if (!pageExists(oldState, action.existedPageTag)
					|| pageExists(oldState, action.page)) {
				oldState
			} else {
				action
						.page
						.updateData(pageInteractionStrategy::onAdded)
						.let { oldState.addPageToPosition(it, pageIndex(oldState, action.existedPageTag)) }
			}

	private fun reduceRemovePage(oldState: PagesState<Page>, action: PagesAction.RemovePage<Page>): PagesState<Page> =
			if (!pageExists(oldState, action.pageTag)) {
				oldState
			} else {
				val currentPage = action.pageTag == oldState.currentPageTag
				val pageIndex = pageIndex(oldState, action.pageTag)
				oldState
						.updatePageAtPosition(pageIndex) {
							it
									.let {
										if (currentPage)
											pageInteractionStrategy.onBecomeNotCurrent(it)
										else
											it
									}
									.let(pageInteractionStrategy::onRemoved)
						}
						.removePageAtPosition(pageIndex)
						.copy(currentPageTag = if (currentPage) null else oldState.currentPageTag)
			}

	private fun reduceNavigateToPage(oldState: PagesState<Page>, action: PagesAction.NavigateToPage<Page>): PagesState<Page> =
			if (!pageExists(oldState, action.pageTag)
					|| action.pageTag == oldState.currentPageTag) {
				oldState
			} else {
				navigateToPage(oldState, action.pageTag)
			}

	private fun reduceNavigateBack(oldState: PagesState<Page>, action: PagesAction.NavigateBack<Page>): PagesState<Page> =
			if (oldState.currentPageTag == null) {
				oldState
			} else {
				oldState.pages.data.getOrNull(pageIndex(oldState, oldState.currentPageTag) - 1)
						?.tag
						?.let { navigateToPage(oldState, it) }
						?: oldState
			}

	private fun reduceNavigateForward(oldState: PagesState<Page>, action: PagesAction.NavigateForward<Page>): PagesState<Page> =
			if (oldState.currentPageTag == null) {
				oldState
			} else {
				oldState.pages.data.getOrNull(pageIndex(oldState, oldState.currentPageTag) + 1)
						?.tag
						?.let { navigateToPage(oldState, it) }
						?: oldState
			}

	private fun reduceCompositePageAction(oldState: PagesState<Page>, action: PagesAction.CompositePageAction<Page>): PagesState<Page> =
			action
					.actions
					.fold(oldState, this::reduce)

	//TODO
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


	private fun <Page> PagesState<Page>.addPageToPosition(page: Tag<Page>, position: Int) =
			updatePages { it.toMutableList().apply { add(position, page) }.toList() }

	private fun <Page> PagesState<Page>.removePageAtPosition(position: Int) =
			updatePages { it.toMutableList().apply { removeAt(position) }.toList() }

	private fun <Page> PagesState<Page>.updatePageAtPosition(position: Int, updatePage: (Page) -> Page) =
			updatePages { it.toMutableList().apply { add(position, removeAt(position).updateData(updatePage::invoke)) }.toList() }

	private fun <Page> PagesState<Page>.updatePages(updatePages: (List<Tag<Page>>) -> List<Tag<Page>>) =
			copy(pages = pages.newVersion(data = updatePages(pages.data)))

	private fun <Data> Tag<Data>.updateData(updateData: (Data) -> Data) =
			copy(data = updateData(data))

	private fun PagesReducer<Page>.navigateToPage(oldState: PagesState<Page>, pageTag: String): PagesState<Page> =
			oldState
					.let {
						it
								.currentPageTag
								?.let { currentPageTag ->
									it.updatePageAtPosition(pageIndex(it, currentPageTag), pageInteractionStrategy::onBecomeNotCurrent)
								} ?: it
					}
					.updatePageAtPosition(pageIndex(oldState, pageTag), pageInteractionStrategy::onBecomeCurrent)
					.copy(currentPageTag = pageTag)
}