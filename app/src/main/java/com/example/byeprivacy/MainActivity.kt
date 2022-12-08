package com.example.byeprivacy

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.google.android.material.bottomnavigation.BottomNavigationView


object CheckinBar{
    var checkedBar: String = ""
    var checkedbool: Boolean = false
}
class MainActivity : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView

        bottomNav.setOnItemSelectedListener {item->
            when (item.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.action_global_barsFragment)
                    return@setOnItemSelectedListener true

                }
                R.id.profile -> {
                    PreferenceData.getInstance().clearData(applicationContext)
                    navController.navigate(R.id.loginFragment)
                    return@setOnItemSelectedListener true
                }

                R.id.check_in -> {
                    navController.navigate(R.id.action_global_barsWithLocation)
                    return@setOnItemSelectedListener true

                }
                R.id.social->{
                    navController.navigate(R.id.action_global_friendsFragment)
                    return@setOnItemSelectedListener true

                }
                else->    return@setOnItemSelectedListener false

            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("destination",destination.displayName)

            when (destination.id) {

                R.id.loginFragment -> hideBottomNav()
                R.id.signUpFragment -> hideBottomNav()
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
