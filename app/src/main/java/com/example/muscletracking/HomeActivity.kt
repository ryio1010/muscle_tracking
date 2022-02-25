package com.example.muscletracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.viewmodel.menu.MenuViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val menuViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            MenuViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 下部メニューの設定
        val bottomNavView = findViewById<BottomNavigationView>(R.id.MenuBottom)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        bottomNavView.setupWithNavController(navController!!.findNavController())

        // トレーニングメニュー取得API実行
        val userId = intent.getStringExtra("userId")
        menuViewModel.getAllMenu(userId!!)

        // observer登録
        menuViewModel.menuList.observe(this, Observer {
            for (menuResponse in it) {
                val menu = Menu(menuResponse.menuId,menuResponse.menuName,menuResponse.musclePart)
                menuViewModel.insertMenu(menu)
            }
        })
    }
}