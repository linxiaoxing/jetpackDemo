package com.example.navigationcomponentstutorials.fragment.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.databinding.FragmentMainBinding
import com.example.navigationcomponentstutorials.fragment.blankfragment.BaseDataBindingFragment
import com.example.navigationcomponentstutorials.fragment.util.Event
import com.example.navigationcomponentstutorials.setupWithNavController
import com.example.navigationcomponentstutorials.viewmodel.NavControllerViewModel

class MainFragment : BaseDataBindingFragment<FragmentMainBinding>() {
   override fun getLayoutRes(): Int = R.layout.fragment_main

   private val navControllerViewModel by activityViewModels<NavControllerViewModel>()


   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      if (savedInstanceState == null) {
         setupBottomNavigationBar()
      }
   }

   private fun setupBottomNavigationBar() {
      val bottomNavigationView = dataBinding!!.bottomNav

      val navGraphIds = listOf(
         R.navigation.nav_graph_view_pager,
         R.navigation.nav_graph_dashboard,
         R.navigation.nav_graph_notification
      )

      // Setup the bottom navigation view with a list of navigation graphs
      val controller = bottomNavigationView.setupWithNavController(
         navGraphIds = navGraphIds,
         fragmentManager = childFragmentManager,
         containerId = R.id.containerNavHostFragment,
         intent = requireActivity().intent
      )

      // Whenever the selected controller changes, setup the action bar.
      subscribeBottomNavigation(controller)

      subscribeAppbarNavigation()

      addNotificationBadge()
   }

   private fun subscribeBottomNavigation(controller: LiveData<NavController>) {
      controller.observe(viewLifecycleOwner, Observer { navController ->
         controller.observe(viewLifecycleOwner, Observer { navController ->
            val appBarConfig = AppBarConfiguration(navController.graph)
            dataBinding!!.toolbar.setupWithNavController(navController, appBarConfig)
         })
      })
   }

   private fun subscribeAppbarNavigation() {
      navControllerViewModel.currentNavController.observe(viewLifecycleOwner, Observer { it ->

         it?.let { event: Event<NavController?> ->
            event.getContentIfNotHandled()?.let { navController ->
               val appBarConfig = AppBarConfiguration(navController.graph)
               dataBinding!!.toolbar.setupWithNavController(navController, appBarConfig)
            }
         }
      })
   }

   private fun addNotificationBadge() {
      // Add badge to bottom navigation
      val bottomNavigationView = dataBinding!!.bottomNav
      val menuItemId = bottomNavigationView.menu.getItem(2).itemId
      val badge = bottomNavigationView.getOrCreateBadge(menuItemId)
      badge.number = 2
   }
}