package com.example.navigationcomponentstutorials.fragment.blankfragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.databinding.FragmentNotification1Binding

class NotificationFragment1 : BaseDataBindingFragment<FragmentNotification1Binding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_notification1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = dataBinding!!

        binding.btnNextPage.setOnClickListener {
            findNavController().navigate(R.id.action_notificationFragment1_to_notificationFragment2)
        }

    }

}