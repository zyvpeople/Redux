package com.develop.zuzik.redux.model.lock

import com.develop.zuzik.redux.core.Action

/**
 * Created by yaroslavzozulia on 7/4/17.
 */
internal sealed class LockAction : Action {
	class Lock : LockAction()
	class Unlock : LockAction()
}