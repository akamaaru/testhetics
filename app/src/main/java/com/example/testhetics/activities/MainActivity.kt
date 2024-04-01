package com.example.testhetics.activities

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.testhetics.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.explore_fragment,
            R.id.create_fragment,
            R.id.profile_fragment
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationBar.setupWithNavController(navController)
    }
}