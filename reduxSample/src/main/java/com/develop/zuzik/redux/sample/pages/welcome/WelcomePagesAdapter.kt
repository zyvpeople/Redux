package com.develop.zuzik.redux.sample.pages.welcome

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.develop.zuzik.redux.core.Tag

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class WelcomePagesAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

	var pages: List<Tag<WelcomePage>> = listOf()
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun getItem(position: Int): Fragment = WelcomePageFragment.create(pages[position].data)

	override fun getCount(): Int = pages.size
}