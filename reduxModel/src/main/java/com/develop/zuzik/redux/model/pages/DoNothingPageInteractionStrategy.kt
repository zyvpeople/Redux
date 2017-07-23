package com.develop.zuzik.redux.model.pages

/**
 * Created by yaroslavzozulia on 7/17/17.
 */
class DoNothingPageInteractionStrategy<Page> : PageInteractionStrategy<Page> {
	override fun onAdded(page: Page): Page = page
	override fun onRemoved(page: Page): Page = page
	override fun onBecomeCurrent(page: Page): Page = page
	override fun onBecomeNotCurrent(page: Page): Page = page
}