package com.skyyo.template.features.thirdTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skyyo.template.databinding.FragmentThirdTabBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding

@AndroidEntryPoint
class ThirdTabFragment : Fragment() {

    private lateinit var binding: FragmentThirdTabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentThirdTabBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyInsets()
    }

    private fun applyInsets() {
        binding.tvTitle.applySystemWindowInsetsToPadding(top = true)
        // etc
    }
}
