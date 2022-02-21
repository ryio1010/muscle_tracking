package com.example.muscletracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.MenuBottom)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        bottomNavView.setupWithNavController(navController!!.findNavController())
    }
}