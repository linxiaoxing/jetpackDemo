package com.example.navigationcomponentstutorials.fragment.navhost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.databinding.FragmentNavhostPostHorizontalBinding
import com.example.navigationcomponentstutorials.fragment.blankfragment.BaseDataBindingFragment
import com.example.navigationcomponentstutorials.fragment.util.Event
import com.example.navigationcomponentstutorials.viewmodel.NavControllerViewModel

class PostGridNavHostFragment :
    BaseDataBindingFragment<FragmentNavhostPostHorizontalBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_navhost_post_grid

    private val navControllerViewModel by activityViewModels<NavControllerViewModel>()

    private var navController: NavController? = null

    private val nestedNavHostFragmentId = R.id.nestedPostGridNavHostFragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(nestedNavHostFragmentId) as? NavHostFragment
        navController = nestedNavHostFragment?.navController

    }

    override fun onResume() {
        super.onResume()
        // Set this navController as ViewModel's navController
        navControllerViewModel.currentNavController.value = Event(navController)
    }

    override fun onDestroyView() {
        navController = null
        navControllerViewModel.currentNavController.value = Event(null)
        super.onDestroyView()
    }

}