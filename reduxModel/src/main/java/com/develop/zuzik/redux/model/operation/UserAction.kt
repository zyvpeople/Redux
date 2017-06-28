package com.develop.zuzik.redux.model.operation

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
internal sealed class UserAction<Data> {
	class Execute<Data>(val data: Data) : UserAction<Data>()
	class Cancel<Data>(val data: Data) : UserAction<Data>()
	class Clear<Data> : UserAction<Data>()
}