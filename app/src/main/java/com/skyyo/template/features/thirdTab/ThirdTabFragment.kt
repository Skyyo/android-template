package com.skyyo.template.features.thirdTab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.skyyo.template.R
import com.skyyo.template.databinding.FragmentThirdTabBinding
import com.skyyo.template.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class ThirdTabFragment : Fragment(R.layout.fragment_third_tab) {

    private val binding by viewBinding(FragmentThirdTabBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyInsets()
    }

    private fun applyInsets() {
        binding.tvTitle.applyInsetter { type(statusBars = true) { padding(top = true) } }
    }
}
