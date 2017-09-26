package com.develop.zuzik.redux.model.pages

/**
 * Created by yaroslavzozulia on 7/23/17.
 */
data class AddPageData<out Page>(val page: Tag<Page>,
								 val existedPageTag: String)