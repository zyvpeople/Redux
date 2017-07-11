package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.Action
import com.develop.zuzik.redux.core.Reducer

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class PagesReducer<Page> : Reducer<PagesState<Page>> {

	override fun reduce(oldState: PagesState<Page>, action: Action): PagesState<Page> =
			(action as? PagesAction<Page>)?.let {
				reduce(oldState, it)
			} ?: oldState

	private fun reduce(oldState: PagesState<Page>, action: PagesAction<Page>): PagesState<Page> =
			when (action) {
				is PagesAction.NavigateToPage -> {
					if (oldState.pages.data.contains(action.page)) {
						oldState.copy(currentPage = action.page)
					} else {
						oldState
					}
				}
				is PagesAction.AddPage -> TODO()
				is PagesAction.RemovePage -> TODO()
				is PagesAction.CompositePageAction ->
					action.actions.fold(oldState, this::reduce)
			}
}