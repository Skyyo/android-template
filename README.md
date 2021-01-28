# android-template
Template repo with various common components, to reduce "project setup" time

1. [Single navigation graph](https://github.com/Skyyo/android-template)
2. [Single navigation graph & Room ](https://github.com/Skyyo/android-template/tree/room)
3. [Single navigation graph & Proto Store ](https://github.com/Skyyo/android-template/tree/proto_store)
4. [Bottom navigation view](https://github.com/Skyyo/android-template/tree/bottom_navigation_view)
5. [Bottom navigation view & Room ](https://github.com/Skyyo/android-template/tree/bottom_navigation_view_room)
6. [Bottom navigation view & Proto Store ](https://github.com/Skyyo/android-template/tree/bottom_navigation_view_proto)

After setup:
1. Change package name everywhere ( including proto files )
2. Update / remove readme.md


# During developement (Code style recomendations)

* Always attempt to have a single line ```if/else``` statement. In that case braces aren't needed. If the ```if/else``` expression exceeds single line - please use braces. If only an ```if``` is used and expression is a single line - braces aren't needed. 
```kotlin
private fun provideCardColor(): Int = if (x > y) Color.CYAN else Color.BLUE
```
```kotlin
if (condition == true) doSmth()
..
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
instead of:
```kotlin
private fun validateSmth() {
        if (condition == true) state.postValue(stateX)
        else events.offer(PasswordNotValid)
    }
``` 
* Explicitly specify function return types if function output type isn't obvious from function name, or functions code block at first glance.

use:
```kotlin
private fun provideSomeValue(): Int = (12f * 23).toInt() + 123
```
instead of:
```kotlin
private fun provideSomeValue() = (12f * 23).toInt() + 123
```
* It's ok to use [trailing commas](https://kotlinlang.org/docs/reference/coding-conventions.html#trailing-commas). They are there to make our life easier.
```kotlin
data class SignUpRequest(
    val email: String,
    val firstName: String,
)
```
* Use [typealiases](https://kotlinlang.org/docs/reference/type-aliases.html#type-aliases) if type name is too long or we have a lot of recurring lambda types.
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
```typealias``` should be declared in an appropriate scope. If used in a single place - it can be placed on top of the same class. If it's a common lambda, which can be reused across different feature packages - please create ```CommonTypeAliases``` file under common package.

* We are extensively using ```tryOrNull```extension when we don't need to check the exception reasons from ```try/catch``` blocks, which allows us to write concise statements like in example below, by using kotlins nullability with [elvis operator](https://kotlinlang.org/docs/reference/null-safety.html#elvis-operator)
```kotlin
suspend fun checkIfEmailExists(email: String): EmailExistsResult {
        val result = tryOrNull { authCalls.checkIfEmailExists(email) }
        result ?: return EmailExistsNetworkError
        return if (result.isUserRegisteredByEmail) EmailExists else EmailNotFound
    }
```

* Handle [process death](https://developer.android.com/topic/libraries/architecture/saving-states#onsaveinstancestate). We are using [SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate) inside viewModel 99% of the time. There is difference between return types. We need to manually save the values when using ```non-liveData``` fields. ```LiveData``` value reassignments will be automatically reflected inside ```savedStateHandle```.
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


# Before release
* Check the app for [overdrawing](https://developer.android.com/topic/performance/rendering/overdraw) regions, and optimize wherever possible.
* Check all ```recyclerViews``` usages and apply ```setHasFixedSize(true)``` wherever applicable.
* Run IDE's ```remove unused resources```. Be carefull to check the changes before commiting, so you don't accidentaly remove classes, which are just temporarily unused.
* Run IDE's ```convert png's to webp's```.
* Check the [r8](https://developer.android.com/studio/build/shrink-code#enable) rules to prevent release .apk/.aab issues as much as possible.
