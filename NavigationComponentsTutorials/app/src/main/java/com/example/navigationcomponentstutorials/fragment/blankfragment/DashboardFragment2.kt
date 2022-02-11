package com.example.navigationcomponentstutorials.fragment.blankfragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.databinding.FragmentDashboard1Binding
import com.example.navigationcomponentstutorials.databinding.FragmentDashboard2Binding

class DashboardFragment2 : BaseDataBindingFragment<FragmentDashboard2Binding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = dataBinding!!

        binding.btnNextPage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment2_to_dashboardFragment3)
        }
    }
}