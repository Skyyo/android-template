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
* Inside viewModel, refer to the following placement of functions & variables
```kotlin
class ViewModel(..){
 // variables
 
 // init{}
 
 // ui callbacks, eg. onBackPressed(), onNextButtonClick(), onEmailEntered()
 
 // db/network related functions

 // helper functions, transformations etc.
 
 // navigation functions
}
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
Always attempt to use [references](https://kotlinlang.org/docs/reflection.html#function-references) instead of lambdas.
```kotlin
QuestionContent(
         question = question,
         onIdAnswerGiven = viewModel::onIdAnswerGiven,
          ..
         )
```
* Unless constant is used across different classes, declare it as `const val NAME = VALUE` on top of the calling class.

* Be pragmatic when writing `composables`. As a rule of a thumb - just allow passing `Modifier` to the composable,  unless it's very specific and is a part of a single not reusable composable, eg. if you have an `AppBar` on a certain screen which is very different from `AppBar` that is used in 99% of the app - it makes no sense to pass other arguments to the latter, just to use single composable everywhere.

* Handle [process death](https://developer.android.com/topic/libraries/architecture/saving-states#onsaveinstancestate), at minimum in places with user input and other critical to lose temporary state. We are using [SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate) inside viewModel 99% of the time. There is difference between return types. We need to manually save the values when using ```non-liveData``` fields. ```LiveData``` value reassignments will be automatically reflected inside ```savedStateHandle```. For testing purposes please use [venom](https://github.com/YarikSOffice/venom).
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
Also remember about ```rememberSaveable```. Sometimes it's preffered to persist view state inside view itself. eg. when we want to remember last active Tab, and don't do anything with it on the viewModel layer. 

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

## Unit testing

* Always preffer fakes over mocks whenever possible. It has a noticable effect on time test needs to run.
* Be as descriptive as possible when naming the test
* Also don't use `Update email when onEmailEntered() is invoked() = runTest {`, always use braces so that function name is always visible when function bodies are folded
```kotlin
    @Test
    fun `Update email when onEmailEntered() is invoked`() {
       runTest {
        // given
        viewModel.email.value = InputWrapper(value = "example.email@")
        // when
        viewModel.onEmailEntered("example.email@g")
        // then
        viewModel.email.test {
            Assert.assertEquals(InputWrapper(value = "example.email@g"), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
 }
```

* Working with dates/times is done via [java.time](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html#package.description). No need for [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP) or ```java.util.date``` anymore.
For API < 26 versions - just [enable desugaring](https://developer.android.com/studio/write/java8-support#library-desugaring). Also don't be fast with creating extensions, first make yourself familiar with already available methods. There are plenty examples out there, like [this one](https://www.baeldung.com/java-8-date-time-intro). We should rely on [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601). All examples are inside this [sheet](https://docs.google.com/spreadsheets/d/1rSUBATCkLolTeX4VORi14t_Ue3yz5WdJHng1WgM75Qs/edit#gid=0). Template already contains basic usages inside ```DateTimeExtensions.kt```

* Document complex code blocks, custom views, values that represent "types" in network responses, logical flows, etc.
* Take responsibility for keeping libraries updated to the latest versions available. Be very carefull, read all release notes & be prepared that there might be subtle, destructive changes.
* Optimize internet traffic using HEAD requests where makes sense.
* Ensure that you're handling system insets on all screens, so app falls under [edge-to-edge](https://developer.android.com/training/gestures/edge-to-edge) category.
* Never use ```shareIn``` or ```stateIn``` to create a new flow that’s returned when calling a function. [Explanation](https://medium.com/androiddevelopers/things-to-know-about-flows-sharein-and-statein-operators-20e6ccb2bc74) 
* Use [shrinkResources](https://developer.android.com/studio/build/shrink-code)
* Use [firebase dynamic links](https://firebase.google.com/docs/dynamic-links) for deep links
* Be very careful when choosing between ```liveData``` & ```stateFlows``` because ```stateFlow``` can't reproduce a certain behaviour in "search-like" scenarios:
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

# Compose related
* I'd recommend reading [this](https://skyyo.medium.com/performance-in-jetpack-compose-9a85ce02f8f9) article about performance related things in Compose
* Always use [remember](https://developer.android.com/jetpack/compose/state#state-in-composables) for anything that can allocate memory but can be created only once, is taking time to get calculated and unstable lambdas.
* Use [collectAsStateWithLifecycleImmutable](https://gist.github.com/Skyyo/378fe96e610557026be4f5f7c2df133b) as a workaround when passing Collections & 3d party lirbary objects which are not inferred stable by compose compiler to ensure that Composables which take List for exampe won't recompose for no reason. Alternative would be [kotlinx.collections.immutable](https://github.com/Kotlin/kotlinx.collections.immutable) but seems perfromance wise it's a bigger evil than a wrapper, also this [issue](https://issuetracker.google.com/issues/254435410) is making it even worse

* Be pragmatic with creating composables. If some element is specific to the screen, it's not necessary to provide constructor with parameters to it. If composable is going to be used in different places - then providing modifier and other params makes sense.
* When observing events and the ```when``` becomes big enough, please use the following:
```kotlin
LaunchedEffect(Unit) { 
    observeEvents(events) 
}
```

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
* Check if cold startup time is good. If it's not - try to use [app startup library](https://developer.android.com/topic/libraries/app-startup) in case when there is plenty of ContentProviders. Also take a look if smth can be lazy initialized if it's not used immediately upon app start. Here is a [library](https://github.com/okcredit/android-cold-startup-instrumentation) which allows to monitor the amount of ms needed for content providers to be initialized. One of the approach of measuring the startup time is nicely described [here](https://medium.com/androiddevelopers/testing-app-startup-performance-36169c27ee55) (using a bash script).

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
* Leave code in a better shape than it was before even you've changed few lines in a file.

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
