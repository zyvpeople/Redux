package com.develop.zuzik.redux.sample.pages.welcome

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import com.develop.zuzik.redux.model.pages.Tag

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class WelcomePagesAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

	private val cachedFragments = HashMap<String, Fragment>()


	var pages: List<Tag<WelcomePage>> = listOf()
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun getItem(position: Int): Fragment = WelcomePageFragment.create(pages[position].data)

	override fun getCount(): Int = pages.size

	override fun getItemPosition(fragment: Any?): Int {
		val tagForFragment = tagForFragment(fragment as? Fragment)
		val childWithTagExists = pages.firstOrNull { it.tag == tagForFragment } != null
		return if (childWithTagExists) {
			super.getItemPosition(fragment)
		} else {
			PagerAdapter.POSITION_NONE
		}
	}

	override fun instantiateItem(container: ViewGroup?, position: Int): Any {
		val fragment = super.instantiateItem(container, position) as Fragment
		cachedFragments += pages[position].tag to fragment
		return fragment
	}

	override fun destroyItem(container: ViewGroup?, position: Int, fragment: Any?) {
		tagForFragment(fragment as? Fragment)?.let {
			cachedFragments.remove(it)
		}
		super.destroyItem(container, position, fragment)
	}

	private fun tagForFragment(fragment: Fragment?) =
			cachedFragments
					.entries
					.firstOrNull { it.value === fragment }
					?.key
}