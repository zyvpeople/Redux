package com.develop.zuzik.redux.model.operation

/**
 * Created by yaroslavzozulia on 6/28/17.
 */
internal sealed class UserAction<Input> {
	class Execute<Input>(val input: Input) : UserAction<Input>()
	class Cancel<Input>(val input: Input) : UserAction<Input>()
	class Clear<Input> : UserAction<Input>()
}