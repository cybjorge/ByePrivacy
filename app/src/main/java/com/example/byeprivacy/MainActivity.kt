package com.example.byeprivacy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.byeprivacy.ui.fragments.BarsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var bottomNav: BottomNavigationView

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //bottom navigation
        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.signUpFragment || nd.id == R.id.loginFragment) {
                bottomNav.visibility = View.VISIBLE
            } else {
                bottomNav.visibility = View.GONE
            }


            bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
            bottomNav.setOnNavigationItemReselectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        navController.navigate(R.id.action_global_barsFragment)
                    }
                    /*
                R.id.message -> {
                    loadFragment(ChatFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.settings -> {
                    loadFragment(SettingFragment())
                    return@setOnNavigationItemReselectedListener
                }
                */
                }
            }
        }

    }
}