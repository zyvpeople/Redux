package com.develop.zuzik.redux.model.pages

import com.develop.zuzik.redux.core.Tag
import com.develop.zuzik.redux.core.Version

/**
 * Created by yaroslavzozulia on 7/9/17.
 */
data class PagesState<Page>(
		val pages: Version<List<Tag<Page>>>,
		val currentPageTag: String?)