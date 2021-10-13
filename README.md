# android-template
Template repo with various common components, to reduce "project setup" time

Fragmentless template can be found [here](https://github.com/Skyyo/android-compose-template)

1. [Single navigation graph](https://github.com/Skyyo/android-template)
2. [Single navigation graph & Room ](https://github.com/Skyyo/android-template/tree/room)
3. [Single navigation graph & Proto Store ](https://github.com/Skyyo/android-template/tree/proto_store)
4. [Bottom navigation view](https://github.com/Skyyo/android-template/tree/bottom_navigation_view)
5. [Bottom navigation view & Room ](https://github.com/Skyyo/android-template/tree/bottom_navigation_view_room)

After setup:
1. Change package name everywhere ( including proto files ). [Link to painless solution](https://stackoverflow.com/questions/16804093/rename-package-in-android-studio/35057550#35057550)
2. Update readme.md


# During developement (Code style/approaches recomendations)

* Always attempt to have a single line ```if/else``` statement. In that case braces aren't needed. If the ```if/else``` expression exceeds single line - please use braces. If only an ```if``` is used and expression is a single line - braces aren't needed. Also consider using ```when``` for value assignments, most of the times it's a better fit than ```if/else``` flow.
```kotlin
private fun provideCardColor(): Int = if (x > y) Color.CYAN else Color.BLUE
```
```kotlin
if (condition == true) doSmth()
..
```
instead of:
```kotlin
private fun validateSmth() {
        if (condition == true) state.postValue(stateX)
        else events.offer(PasswordNotValid)
    }
``` 
use:
```kotlin
private fun validateSmth() {
        if (condition == true) {
            state.postValue(stateX)
        } else {
            events.offer(PasswordNotValid)
        }
    }
```    
* Preffering if/else to let/run avoids cryptic bugs:
```kotlin
var x: String? = "0"
        x?.let {
            //let block will be executed
            executeSmth()
        } ?: run {
            //run block will be also executed since executeSmth() returned null
        }
fun executeSmth(): String? = null
```  

* Rule of a thumb: if we access the same object 3 or more times - please use ```apply```,```with```. If we want to hide big, non-priority (glance wise) code chunks - there is nothing wrong to use ```apply```, with even a 1 line under it.
* Explicitly specify function return types if function output type isn't obvious from function name, or functions code block at first glance.

instead of:
```kotlin
private fun provideSomeValue() = (12f * 23).toInt() + 123
```
use:
```kotlin
private fun provideSomeValue(): Int = (12f * 23).toInt() + 123
```

* It's ok to use [trailing commas](https://kotlinlang.org/docs/reference/coding-conventions.html#trailing-commas). They are there to make our life easier.
```kotlin
data class SignUpRequest(
    val email: String,
    val firstName: String,
)
```

* Use [typealiases](https://kotlinlang.org/docs/reference/type-aliases.html#type-aliases) if type name is too long or we have a lot of recurring lambda types.
```typealias``` should be declared in an appropriate scope. If used in a single place - it can be placed on top of the same class. If it's a common lambda, which can be reused across different feature packages - please create ```CommonTypeAliases``` file under common package.

instead of:
```kotlin
class ShopProductItem(
    private val onClick: (position: Int) -> Unit
)
```
use 
```kotlin
typealias OnClick = (position: Int) -> Unit
class ShopProductItem(
    private val onClick: OnClick
)
```

* We are extensively using ```tryOrNull```extension when we don't need to check the exception reasons from ```try/catch``` blocks, which allows us to write concise statements like in example below, by using kotlins nullability with [elvis operator](https://kotlinlang.org/docs/reference/null-safety.html#elvis-operator)
```kotlin
suspend fun checkIfEmailExists(email: String): EmailExistsResult {
        val result = tryOrNull { authCalls.checkIfEmailExists(email) }
        result ?: return EmailExistsNetworkError
        return if (result.isUserRegisteredByEmail) EmailExists else EmailNotFound
    }
```

* Handle [process death](https://developer.android.com/topic/libraries/architecture/saving-states#onsaveinstancestate). We are using [SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate) inside viewModel 99% of the time. There is difference between return types. We need to manually save the values when using ```non-liveData``` fields. ```LiveData``` value reassignments will be automatically reflected inside ```savedStateHandle```. For testing purposes please use [venom](https://github.com/YarikSOffice/venom).
```kotlin
val state = handle.getLiveData("viewState", 0)
state.postValue(1) // doing so will automatically give us value of 1 upon PD

var scanCompleted = handle.get<Boolean>("scanCompleted") ?: false
        set(value) {
            field = value
            handle.set("scanCompleted", value) // set the key/value pair to the bundle upon each value reassignment
        }
scanCompleted = true // will be "false" after PD, unless we set the key/value pair to the bundle like above

```

* Declare custom classes instead of [Pairs](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/#pair), [Tripples](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-triple/).

instead of:
```kotlin
val timeRange: Pair<Int, Int>? = null
```
use:
```kotlin
class TimeRange(val from: Int, val to: Int)
val timeRange: TimeRange? = null
```

* When composing objects, consider the following approach:

instead of:
```kotlin
when (val response = authRepo.verifyCode(
            VerifyCodeRequest(
                email, pin,
                "someValue", "someValue",
                "someValue", "someValue"
            )
        )) {
            is VerifyCodeSuccess -> signIn()
            ..
        }
```
use:
```kotlin
val requestBody = VerifyCodeRequest(email, pin)
        when (val response = authRepo.verifyCode(requestBody)) {
            is VerifyCodeSuccess -> signIn()
            ..
        }
        //if object constructor has many arguments,or has some additional logic - move it into provideX() function, like this:
val requestBody = provideVerifyCodeRequestBody()
        when (val response = authRepo.verifyCode(requestBody)) {
            is VerifyCodeSuccess -> signIn()
            ..
        }
```

* Be pragmatic with [Kotlin Named Arguments](https://kotlinlang.org/docs/functions.html#named-arguments). Use them to make parts that are not self documenting easier to read:
```kotlin
class SignUpRequestBody(val email: String, val password: String)
val requestBody = SignUpRequestBody(email, password)

class ItemDecorator(val paddingTop: Int, val paddingLeft: Int, val paddingBottom: Int)
val itemDecorator = ItemDecorator(
    paddingTop = 16,
    paddingLeft = 8,
    paddingBottom = 2
)
```

* Working with dates/times is done via [java.time](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html#package.description). No need for [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP) or ```java.util.date``` anymore.
For API < 26 versions - just [enable desugaring](https://developer.android.com/studio/write/java8-support#library-desugaring). Also don't be fast with creating extensions, first make yourself familiar with already available methods. There are plenty examples out there, like [this one](https://www.baeldung.com/java-8-date-time-intro). We should rely on [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601). All examples are inside this [sheet](https://docs.google.com/spreadsheets/d/1rSUBATCkLolTeX4VORi14t_Ue3yz5WdJHng1WgM75Qs/edit#gid=0). Template already contains basic usages inside ```DateTimeExtensions.kt```

* Document complex code blocks, custom views, values that represent "types" in network responses, logical flows, etc.
* Take responsibility for keeping libraries updated to the latest versions available. Be very carefull, read all release notes & be prepared that there might be subtle, destructive changed.
* Optimize internet traffic using HEAD requests where makes sense.
* Ensure that you're handling system insets on all screens, so app falls under [edge-to-edge](https://developer.android.com/training/gestures/edge-to-edge) category.
* Never use ```shareIn``` or ```stateIn``` to create a new flow that’s returned when calling a function. [Explanation](https://medium.com/androiddevelopers/things-to-know-about-flows-sharein-and-statein-operators-20e6ccb2bc74) 
* Use [shrinkResources](https://developer.android.com/studio/build/shrink-code)
* Use [firebase dynamic links](https://firebase.google.com/docs/dynamic-links) for deep links
* Be very careful when choosing between ```liveData``` & ```stateFlows```. 
We still can't drop ```liveData``` not only because we need it in the ```savedStateHandle.getLiveData<Key>``` scenarios, but because ```stateFlow``` can't reproduce a certain behaviour in "search-like" scenarios:
```kotlin
class ViewModel(repository: Repository) : ViewModel() {

    private val query = MutableStateFlow("")
    
    val results: Flow<Result> = query.flatMapLatest { query ->
        repository.search(query)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Result()
    )
    
   fun onQueryChanged(query: String) { query.value = query }
}
```
* Always use ```liveData``` for cases when we are performing ```observable.switchMap/flatMapLatest``` type of operations. In code above it's the ```query```, it has to be declared as ```liveData```. You can always observe it using ```.asFlow```
Behaviour difference is explained [here](https://github.com/Kotlin/kotlinx.coroutines/issues/2223) 
* Be carefull how you update the ```stateFlow``` value, since using ```stateFlow.value = stateFlow.value.copy()``` can create unexpected results. If between the time copy function completes and the ```stateFlows``` new value is emitted another thread tries to update the ```stateFlow``` — by using copy and updating one of the properties that the current copy isn’t modifying — we could end up with results we were not expecting. So please use [update](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/update.html) in such cases. 


# Continuous integration & pull requests
* Template already has a few GitHub Actions workflows included. Please ensure you're passing the checks locally, before opening pull request. To do that, either run commands in the IDE terminal, or setup a github hook. Commands are: ```./gradlew ktlintFormat```, ```./gradlew detektDebug```. <b>Request a review only after the CI checks have passed successfully</b>.
* If pull request contains code that should close the issue, please write: ```close #1, close #2```(number == issue number) somewhere in the PR description. This allows for automatic issue closing upon successfull PR merge.
* Commit code as many times as you want while working on a feature. When the feature is ready - do a careful rebase over origin/master and squash all this stuff into one or two meaningful commits that clearly represent the feature, before opening a pull request.
* Features should be splitted into logical chunks if they require a lot of code changes.
* Attempt to keep PR size in range of 250 - 300 lines of code changed.


# Before release
* Check the app for [overdrawing](https://developer.android.com/topic/performance/rendering/overdraw) regions, and optimize wherever possible.
* Run IDE's ```remove unused resources```. Be carefull to check the changes before commiting, so you don't accidentaly remove classes, which are just temporarily unused.
* Run IDE's ```convert png's to webp's```.
* Check the [r8](https://developer.android.com/studio/build/shrink-code#enable) rules to prevent release .apk/.aab issues as much as possible.
* It won't hurt to use [canary leak](https://square.github.io/leakcanary/) to check whether you don't have serious issues with memory leaks.
* [Strict mode](https://developer.android.com/reference/android/os/StrictMode) might be helpfull to do a few optimizations.
* If we decouple app language from the system language, please use [SplitInstallManager](https://developer.android.com/reference/com/google/android/play/core/splitinstall/SplitInstallManager) or disable ubundling language files using [android.bundle.language.enableSplit = false](https://stackoverflow.com/a/53276459/5704989)

# Additonal advices
* Invest some time into getting used to [IDE shortcuts](https://developer.android.com/studio/intro/keyboard-shortcuts). Doing so will save you a lot of time.
* Guide on how to [offload code execution to the background thread](https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md#the-problem-of-ui-freezes)
* Use [in-app](https://developer.android.com/guide/playcore/in-app-updates) updates to enhance UX. Sometimes we even want to block certain outdated versions. We always prefer in-app updates, but it's ok to create custom solutions according to project specifications.
* Carefully use [in-app reviews](https://developer.android.com/guide/playcore/in-app-review) to ensure that users leave high ratings on Google Play.
* Always use [crashlytics](https://firebase.google.com/products/crashlytics?gclid=Cj0KCQjw38-DBhDpARIsADJ3kjk3-NryoOdZjKE4FADZ2CN0d0asEegcgGh658K2Wtsc2UwtXtvTtKEaAt5wEALw_wcB&gclsrc=aw.ds) to track the crashes.
* Use [auto-fill](https://developer.android.com/guide/topics/text/autofill-optimize) where possible.
* Use scroll indicators for screens which are might not appear scrollable otherwise.
* Attemp to use min/max data models: shrinked *User* model returned from DB for list of users, and complete *User* model for details screen.
* The ```android:allowBackup=true``` tag can lead to a broken app state that can cause constant app crashes. Benefits of using this feature are almost non-existing, so we keep it off by default. [Explanation](https://www.reddit.com/r/androiddev/comments/ov18d2/this_is_why_your_subclassed_application_isnt/)
* Remember that to keep everyone (including yourself) happy, we can always just increase the database schema during developtement, and rely on [fallbackToDestructiveMigration](https://developer.android.com/reference/android/arch/persistence/room/RoomDatabase.Builder#fallbacktodestructivemigration) when using Room. This will prevent people from getting crashes of they don't clear app data, and us from writing migrations during develpment process. Just ensure that you revert the version to 1 for the release.

# License
```
MIT License

Copyright (c) 2021 Denis Rudenko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.```
