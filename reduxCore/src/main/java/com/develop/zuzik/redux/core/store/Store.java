package com.develop.zuzik.redux.core.store;

import com.develop.zuzik.redux.core.store.middleware.CompositeMiddleware;
import com.develop.zuzik.redux.core.store.middleware.ReduceMiddleware;
import com.develop.zuzik.redux.core.store.reducer.CompositeReducer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * yaroslavzozulia
 * 10/4/17.
 */

public class Store<State> {

	private final State defaultState;
	private final List<Observable<Action>> actionObservables;
	private final Middleware<State> middleware;

	public Store(@NotNull State defaultState,
				 @NotNull List<Observable<Action>> actionObservables,
				 @NotNull List<Reducer<State>> reducers,
				 @NotNull List<Middleware<State>> middlewares) {
		this.defaultState = defaultState;
		this.actionObservables = actionObservables;
		List<Middleware<State>> resultMiddlewares = new ArrayList<>(middlewares);
		resultMiddlewares.add(new ReduceMiddleware<>(new CompositeReducer<>(reducers)));
		this.middleware = new CompositeMiddleware<>(resultMiddlewares);
	}

	@NotNull
	public Observable<State> bind(@NotNull final Scheduler scheduler) {
		return Observable.defer(new Callable<ObservableSource<? extends State>>() {
			@Override
			public ObservableSource<? extends State> call() throws Exception {
				final BehaviorSubject<State> stateSubject = BehaviorSubject.createDefault(defaultState);
				return Observable
						.merge(actionObservables)
						.observeOn(scheduler)
						.concatMap(new Function<Action, ObservableSource<? extends State>>() {
							@Override
							public ObservableSource<? extends State> apply(@NonNull Action action) throws Exception {
								return middleware.dispatch(stateSubject.take(1), action);
							}
						})
						.distinctUntilChanged(new BiPredicate<State, State>() {
							@Override
							public boolean test(@NonNull State oldState, @NonNull State newState) throws Exception {
								return oldState == newState;
							}
						})
						.distinctUntilChanged()
						.observeOn(scheduler)
						.doOnNext(new Consumer<State>() {
							@Override
							public void accept(@NonNull State state) throws Exception {
								stateSubject.onNext(state);
							}
						})
						.startWith(defaultState);
			}
		});
	}
}