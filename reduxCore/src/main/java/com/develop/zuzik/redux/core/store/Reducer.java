package com.develop.zuzik.redux.core.store;

import org.jetbrains.annotations.NotNull;

/**
 * Created by yaroslavzozulia on 9/26/17.
 */

public interface Reducer<State> {
	@NotNull
	State reduce(@NotNull State oldState, @NotNull Action action);
}
