package com.skyyo.template.features.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.insets.ProvideWindowInsets
import com.skyyo.template.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            val events = remember(viewModel.events, viewLifecycleOwner) {
                viewModel.events.receiveAsFlow().flowWithLifecycle(
                    viewLifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                )
            }

            LaunchedEffect(Unit) {
                launch {
                    events.collect { event ->
                        when (event) {
                            is SignInEvent.ShowLongToast -> {
                            }
                        }
                    }
                }
            }

            TemplateTheme {
                ProvideWindowInsets {
                    Box(Modifier.fillMaxSize()) {
                        Button(
                            modifier = Modifier.align(Alignment.Center),
                            onClick = viewModel::goHome
                        ) {
                            Text("go home")
                        }
                    }
                }
            }
        }
    }
}
