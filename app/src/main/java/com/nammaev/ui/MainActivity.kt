package com.nammaev.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nammaev.R
import androidx.navigation.ui.NavigationUI
import com.nammaev.di.blockInput
import com.nammaev.di.unblockInput
import com.nammaev.databinding.ActivityMainBinding as MainActivityBinding

class MainActivity : AppCompatActivity() {
    private var binding: MainActivityBinding? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

        }
        NavigationUI.setupWithNavController(binding?.bottomNavHome!!, navController)   //  Navigation bar

    }

    /*override fun onBackPressed() {
        if (navController.currentDestination?.id != null) {
            when (navController.currentDestination?.id) {
                R.id.splashFragment -> {
                    finish()
                }
                else -> navController.navigateUp()
            }
        } else
            super.onBackPressed()
    }*/

    fun blockInput() {
        blockInput(binding?.pbServices)
    }

    fun unblockInput() {
        this unblockInput binding?.pbServices
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
