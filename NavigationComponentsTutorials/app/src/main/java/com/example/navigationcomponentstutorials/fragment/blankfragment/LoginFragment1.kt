package com.example.navigationcomponentstutorials.fragment.blankfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.databinding.FragmentLogin1Binding
import com.example.navigationcomponentstutorials.fragment.util.Event
import com.example.navigationcomponentstutorials.viewmodel.NavControllerViewModel

/**
 * This fragment is added to graph via [ViewPagerContainerFragment]'s  [NavHostFragment]
 */
class LoginFragment1 : BaseDataBindingFragment<FragmentLogin1Binding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_login1

    private val navControllerViewModel by activityViewModels<NavControllerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = dataBinding!!

        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_view_pager_dest_to_loginFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        // Set this navController as ViewModel's navController
        navControllerViewModel.currentNavController.value = Event(findNavController())
    }

}