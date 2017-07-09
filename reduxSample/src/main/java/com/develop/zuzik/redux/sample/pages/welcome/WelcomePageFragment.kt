package com.develop.zuzik.redux.sample.pages.welcome

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.develop.zuzik.redux.R
import kotlinx.android.synthetic.main.fragment_welcome_page.view.*

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
class WelcomePageFragment : Fragment() {

	companion object Factory {

		fun create(title: String) = WelcomePageFragment().apply {
			arguments = Bundle().apply {
				putString("title", title)
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
			inflater?.inflate(R.layout.fragment_welcome_page, null)?.apply {
				tvTitle.text = arguments.getString("title")
			}
}