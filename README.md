# Redux (Guide to Redux for Android in Kotlin)

## Adding Redux to your Project

1. Clone repository
2. Build 'reduxCore' module
3. Add @aar file to your project

## How to use Redux

1. Define default state

```kotlin
val defaultState = ""
```

2. Define action

```kotlin
class AddSymbolAction : Action
```

3. Define reducer

```kotlin
class AddSymbolReducer : Reducer<String> {
    override fun reduce(oldState: String, action: Action): String =
        (action as? AddSymbolReducer)?.let { oldState + "$" } ?: oldState 
}
```

4. Create middleware

```kotlin
class LogActionMiddleware : Middleware<String> {
    override fun dispatch(state: Observable<String>, action: Action): Observable<String> =
        state.doOnNext { Log.d("TAG", action.toString()) }
}
```

5. Create store

```kotlin
val store = Store(
    state, 
    listOf(Observable.just<Action>(AddSymbolAction())), 
    listOf(AddSymbolReducer()), 
    listOf(LogActionMiddleware()))

```

6. Call .bind method with scheduler

```kotlin
store
    .bind(AndroidSchedulers.mainThread())
    .subscribe({ /* render state */ }, { /* handle error */ })
```
