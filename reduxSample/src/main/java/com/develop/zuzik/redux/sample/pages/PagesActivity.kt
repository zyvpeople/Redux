package com.develop.zuzik.redux.sample.pages

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.redux.R
import com.develop.zuzik.redux.core.model.value.Tag
import com.develop.zuzik.redux.core.extension.asObserver
import com.develop.zuzik.redux.model.pages.*
import com.develop.zuzik.redux.sample.pages.welcome.WelcomePage
import com.develop.zuzik.redux.sample.pages.welcome.WelcomePageInteractionStrategy
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
			val page1 = Tag("1", WelcomePage("1", false, false))
			val page2 = Tag("2", WelcomePage("2", false, false))
			val page3 = Tag("3", WelcomePage("3", false, false))
			val page4 = Tag("4", WelcomePage("4", false, false))
			val pages = listOf(page1, page2, page3, page4)
			val model = PagesModel(pages, "2", WelcomePageInteractionStrategy())
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
			override val displayPages: Observer<List<Tag<WelcomePage>>> = displayPages()
			override val onNavigateToPage: Observable<String> = onNavigateToPageByUsingSwipe()
					.mergeWith(onNavigateToPageByUsingButton())
			override val onAddPageToHead: Observable<Tag<WelcomePage>> = btnAddToHead
					.clicks()
					.map { etNewPage.text.toString() }
					.map { Tag(it, WelcomePage(it, false, false)) }
			override val onAddPageToTail: Observable<Tag<WelcomePage>> = btnAddToTail
					.clicks()
					.map { etNewPage.text.toString() }
					.map { Tag(it, WelcomePage(it, false, false)) }
			override val onAddPageAfterPage: Observable<AddPageData<WelcomePage>> = btnAddAfterPage
					.clicks()
					.map { Pair(etNewPage.text.toString(), etExistedPage.text.toString()) }
					.map { AddPageData(Tag(it.first, WelcomePage(it.first, false, false)), it.second) }
			override val onAddPageBeforePage: Observable<AddPageData<WelcomePage>> = btnAddBeforePage
					.clicks()
					.map { Pair(etNewPage.text.toString(), etExistedPage.text.toString()) }
					.map { AddPageData(Tag(it.first, WelcomePage(it.first, false, false)), it.second) }
			override val onRemovePage: Observable<String> = btnRemovePage
					.clicks()
					.map { etNewPage.text.toString() }
			override val navigateToPage: Observer<String> = navigateToPage()
			override val onNavigateBack: Observable<Unit> = btnNavigateBack.clicks()
			override val onNavigateForward: Observable<Unit> = btnNavigateForward.clicks()
		})
	}

	private fun onNavigateToPageByUsingSwipe(): Observable<String> = viewPager
			.pageSelections()
			.filter { 0 <= it && it < adapter.pages.size }
			.map { adapter.pages[it].tag }

	private fun onNavigateToPageByUsingButton(): Observable<String> = btnNavigateToPage
			.clicks()
			.map { etNewPage.text.toString() }

	private fun displayPages() = Consumer<List<Tag<WelcomePage>>> {
		adapter.pages = it
	}.asObserver()

	private fun navigateToPage() = Consumer<String> { pageTag ->
		val pageIndex = adapter.pages.indexOfFirst { it.tag == pageTag }
		if (pageIndex != -1) {
			viewPager.currentItem = pageIndex
		}
	}.asObserver()

	override fun onStop() {
		presenter.onStop()
		super.onStop()
	}
}
