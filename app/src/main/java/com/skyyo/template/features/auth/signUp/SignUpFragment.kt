package com.skyyo.template.features.auth.signUp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.skyyo.template.R
import com.skyyo.template.databinding.FragmentHomeBinding
import com.skyyo.template.extensions.longToast
import com.skyyo.template.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyInsets()
        with(binding) {
            btnDoSmth.setOnClickListener { }
        }
        observeState()
        observeEvents()
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            for (event in viewModel.events)
                when (event) {
                    is ShowLongToast -> longToast(getString(event.stringId))
                }
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                stateX -> {
                } // assembleStateX()
                stateY -> {
                } // assembleStateY()
            }
        }
    }

    private fun applyInsets() {
        binding.tvTitle.applySystemWindowInsetsToPadding(top = true)
    }

    companion object {
        const val stateX = 0
        const val stateY = 1
    }
}
