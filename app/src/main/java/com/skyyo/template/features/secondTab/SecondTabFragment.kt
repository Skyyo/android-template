package com.skyyo.template.features.secondTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skyyo.template.databinding.FragmentSecondTabBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding

@AndroidEntryPoint
class SecondTabFragment : Fragment() {

    private lateinit var binding: FragmentSecondTabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentSecondTabBinding.inflate(inflater)
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
