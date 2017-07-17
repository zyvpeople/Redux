package com.develop.zuzik.redux.sample.pages.welcome

import com.develop.zuzik.redux.model.pages.PageInteractionStrategy

/**
 * Created by yaroslavzozulia on 7/18/17.
 */
class WelcomePageInteractionStrategy : PageInteractionStrategy<WelcomePage> {

	override fun onAdded(page: WelcomePage): WelcomePage =
			page.copy(init = true)

	override fun onRemoved(page: WelcomePage): WelcomePage =
			page.copy(init = false)

	override fun onBecomeCurrent(page: WelcomePage): WelcomePage =
			page.copy(current = true)

	override fun onBecomeNotCurrent(page: WelcomePage): WelcomePage =
			page.copy(current = false)
}