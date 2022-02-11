package com.example.navigationcomponentstutorials.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.navigationcomponentstutorials.fragment.util.Event

class NavControllerViewModel : ViewModel() {
    val currentNavController = MutableLiveData<Event<NavController?>>()
}