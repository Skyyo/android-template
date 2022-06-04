package com.skyyo.template.features.signIn

import androidx.lifecycle.SavedStateHandle
import com.skyyo.template.CoroutineTestRule
import com.skyyo.template.application.repositories.auth.AuthRepository
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private lateinit var viewModel: SignInViewModel

    @Before
    fun setup() {
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        val navigationDispatcher = NavigationDispatcher()
        val authRepository = mockk<AuthRepository>()
        viewModel = SignInViewModel(navigationDispatcher, savedStateHandle, authRepository)
    }

    @Test
    fun `set stateRelatedVariable to true when onEmailEntered() is invoked`() {
//        runTest {
        // given
        viewModel.stateRelatedVariable = false
        // when
        viewModel.onEmailEntered("1")
        // then
        assertEquals(true, viewModel.stateRelatedVariable)
//        }
    }
}
