package com.develop.zuzik.redux.sample.pages

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.extension.asObserver
import com.develop.zuzik.redux.model.pages.Pages
import com.develop.zuzik.redux.model.pages.PagesModel
import com.develop.zuzik.redux.model.pages.PagesPresenter
import com.develop.zuzik.redux.sample.pages.welcome.WelcomePage
import com.develop.zuzik.redux.sample.pages.welcome.WelcomePagesAdapter
import com.jakewharton.rxbinding2.support.v4.view.pageSelections
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_pages.*

class PagesActivity : AppCompatActivity() {

	companion object Models {
		val pagesModel: Pages.Model<WelcomePage> by lazy {
			val page1 = WelcomePage("Page 1")
			val page2 = WelcomePage("Page 2")
			val page3 = WelcomePage("Page 3")
			val page4 = WelcomePage("Page 4")
			val pages = listOf(page1, page2, page3, page4)
			val model = PagesModel(pages, page1)
			model.init()
			model
		}
	}

	private val presenter: Pages.Presenter<WelcomePage> = PagesPresenter(pagesModel)
	private lateinit var adapter: WelcomePagesAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pages)
		adapter = WelcomePagesAdapter(supportFragmentManager)
		viewPager.adapter = adapter
	}

	override fun onStart() {
		super.onStart()
		presenter.onStart(object : Pages.View<WelcomePage> {
			override val displayPages: Observer<List<WelcomePage>> = displayPages()
			override val onNavigateToPage: Observable<WelcomePage> = onNavigateToPageByUsingSwipe()
					.mergeWith(onNavigateToPageByUsingButton())
			override val onAddPageToHead: Observable<WelcomePage> = btnAddToHead
					.clicks()
					.map { etNewPage.text.toString() }
					.map(::WelcomePage)
			override val onAddPageToTail: Observable<WelcomePage> = btnAddToTail
					.clicks()
					.map { etNewPage.text.toString() }
					.map(::WelcomePage)
			override val onAddPageAfterPage: Observable<Pair<WelcomePage, WelcomePage>> = btnAddAfterPage
					.clicks()
					.map { Pair(etNewPage.text.toString(), etExistedPage.text.toString()) }
					.map { Pair(WelcomePage(it.first), WelcomePage(it.second)) }
			override val onAddPageBeforePage: Observable<Pair<WelcomePage, WelcomePage>> = btnAddBeforePage
					.clicks()
					.map { Pair(etNewPage.text.toString(), etExistedPage.text.toString()) }
					.map { Pair(WelcomePage(it.first), WelcomePage(it.second)) }
			override val onRemovePage: Observable<WelcomePage> = btnRemovePage
					.clicks()
					.map { etNewPage.text.toString() }
					.map(::WelcomePage)
			override val navigateToPage: Observer<WelcomePage> = navigateToPage()
			override val onNavigateBack: Observable<Unit> = btnNavigateBack.clicks()
			override val onNavigateForward: Observable<Unit> = btnNavigateForward.clicks()
			override val onSetPages: Observable<List<WelcomePage>> = btnSetPages
					.clicks()
					.map { etNewPages.text.toString() }
					.map { it.split(':').map(::WelcomePage) }
		})
	}

	private fun onNavigateToPageByUsingSwipe(): Observable<WelcomePage> {
		return viewPager
				.pageSelections()
				.filter { 0 <= it && it < adapter.pages.size }
				.map { adapter.pages[it] }
	}

	private fun onNavigateToPageByUsingButton(): Observable<WelcomePage> {
		return btnNavigateToPage
				.clicks()
				.map { etNewPage.text.toString() }
				.map(::WelcomePage)
	}

	private fun displayPages() = Consumer<List<WelcomePage>> {
		adapter.pages = it
	}.asObserver()

	private fun navigateToPage() = Consumer<WelcomePage> {
		val pageIndex = adapter.pages.indexOf(it)
		if (pageIndex != -1) {
			viewPager.currentItem = pageIndex
		}
	}.asObserver()

	override fun onStop() {
		presenter.onStop()
		super.onStop()
	}
}
