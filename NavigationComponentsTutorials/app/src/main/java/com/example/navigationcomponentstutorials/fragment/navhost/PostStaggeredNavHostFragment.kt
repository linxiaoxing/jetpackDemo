package com.example.navigationcomponentstutorials.fragment.navhost


import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.databinding.FragmentNavhostPostStaggeredBinding
import com.example.navigationcomponentstutorials.fragment.blankfragment.BaseDataBindingFragment
import com.example.navigationcomponentstutorials.fragment.util.Event
import com.example.navigationcomponentstutorials.viewmodel.NavControllerViewModel

class PostStaggeredNavHostFragment :
    BaseDataBindingFragment<FragmentNavhostPostStaggeredBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_navhost_post_staggered

    private val navControllerViewModel by activityViewModels<NavControllerViewModel>()

    private var navController: NavController? = null

    private val nestedNavHostFragmentId = R.id.nestedPostStaggeredNavHostFragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(nestedNavHostFragmentId) as? NavHostFragment
        navController = nestedNavHostFragment?.navController

    }

    override fun onResume() {
        super.onResume()

        // Set this navController as ViewModel's navController
        navController?.let {
            navControllerViewModel.currentNavController.value = Event(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navController = null
    }

}