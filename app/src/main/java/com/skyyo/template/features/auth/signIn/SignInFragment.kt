package com.skyyo.template.features.auth.signIn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.skyyo.template.R
import com.skyyo.template.databinding.FragmentSignInBinding
import com.skyyo.template.extensions.longToast
import com.skyyo.template.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyInsets()
        binding.apply {
            tvTitle.text = "Sign in Screen Title"
            btnDoSmth.setOnClickListener {}
        }
        observeEvents()
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                for (event in viewModel.events)
                    when (event) {
                        is EmailValidationError -> {
                        }
                        is ShowLongToast -> longToast(getString(event.stringId))
                    }
            }
        }
    }

    private fun applyInsets() {
        binding.tvTitle.applyInsetter { type(statusBars = true) { padding(top = true) } }
    }
}
