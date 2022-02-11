package com.example.viewpager2sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.viewpager2sample.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val christmasItems = listOf(
            Christmas(
                title = "サンタ",
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.santa)
            ),
            Christmas(
                title = "ツリー",
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.tree)
            ),
            Christmas(
                title = "トナカイ",
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.reindeer)
            ),
            Christmas(
                title = "プレゼント",
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.presents)
            ),
            Christmas(
                title = "ケーキ",
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.cake)
            ),
            Christmas(
                title = "チキン",
                drawable = ContextCompat.getDrawable(requireContext(), R.drawable.chicken)
            )
        )

        val binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = ViewPagerAdapter()

        binding.apply {
            binding.viewpager.adapter = adapter
            adapter.setItem(christmasItems)
            TabLayoutMediator(
                tabLayout,
                viewpager,
                TabLayoutMediator.TabConfigurationStrategy{ _, _ ->
                }
            ).attach()
        }
        binding.viewpager.setPageTransformer(ZoomOutPageTransformer())
        return binding.root
    }
}