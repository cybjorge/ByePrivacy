package com.example.byeprivacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.byeprivacy.ui.fragments.BarsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController



        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView



            bottomNav.setOnItemReselectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        navController.navigate(R.id.action_global_barsFragment)
                    }
                    R.id.profile -> {
                        navController.navigate(R.id.action_global_accountFragment)
                    }
                }
            }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("destination",destination.displayName)

            when (destination.id) {

                R.id.loginFragment -> hideBottomNav()
                R.id.signUpFragment -> showBottomNav()
                else -> showBottomNav()
            }
        }

    }
    private fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNav.visibility = View.GONE

    }

}
