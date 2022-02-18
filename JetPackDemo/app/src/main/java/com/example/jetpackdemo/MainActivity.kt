package com.example.jetpackdemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var mToolbar: Toolbar
    lateinit var mCamera: ImageView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val navController = host.navController

        initWidget()
        initBottomNavigationView(navController)

    }

    private fun initWidget() {
        bottomNavigationView = bnv_view
        mToolbar = toolbar
        mCamera = iv_camera

        mCamera.setOnClickListener {
            // TODO CameraX 学习
        }
    }


    /**
     * Navigation绑定bottomNavigationView
     */
    private fun initBottomNavigationView(navController: NavController) {
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener {  controller, destination, arguments ->
            when (destination.id) {
                R.id.meFragment -> mCamera.visibility = View.VISIBLE
                else -> mCamera.visibility = View.GONE
            }
        }
    }
}