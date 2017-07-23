package com.develop.zuzik.redux.model.pages

/**
 * Created by yaroslavzozulia on 7/15/17.
 */
interface PageInteractionStrategy<Page> {
	fun onAdded(page: Page): Page
	fun onRemoved(page: Page): Page
	fun onBecomeCurrent(page: Page): Page
	fun onBecomeNotCurrent(page: Page): Page
}