package com.example.muscletracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.muscletracking.model.bodycomp.BodyComp
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.model.musclepart.MusclePart
import com.example.muscletracking.model.user.User
import com.example.muscletracking.viewmodel.bodycomp.BodyCompViewModel
import com.example.muscletracking.viewmodel.log.LogViewModel
import com.example.muscletracking.viewmodel.menu.MenuViewModel
import com.example.muscletracking.viewmodel.musclepart.MusclePartViewModel
import com.example.muscletracking.viewmodel.user.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val menuViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            MenuViewModel::class.java
        )
    }
    private val musclePartViewModel: MusclePartViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            MusclePartViewModel::class.java
        )
    }
    private val logViewModel: LogViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(
            LogViewModel::class.java
        )
    }
    private val bodyCompViewModel: BodyCompViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(
            BodyCompViewModel::class.java
        )
    }
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            UserViewModel::class.java
        )
    }

    var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userId = intent.getStringExtra("userId")
        // ログインユーザー情報取得
        userViewModel.selectUserById(userId!!)
        userViewModel.selectUser.observe(this, Observer {
            if (it != null) {
                mUser = it
            }
            android.util.Log.d("mUserDebug", mUser.toString())
        })

        // 下部メニューの設定
        val bottomNavView = findViewById<BottomNavigationView>(R.id.MenuBottom)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        bottomNavView.setupWithNavController(navController!!.findNavController())

        // トレーニングメニュー取得API実行
        menuViewModel.getAllMenu(userId!!)
        menuViewModel.menuList.observe(this, Observer {
            for (menuResponse in it) {
                val menu =
                    Menu(menuResponse.menuId, menuResponse.menuName, menuResponse.musclePartName)
                menuViewModel.insertMenu(menu)
            }
        })

        // トレーニング部位取得API
        musclePartViewModel.getAllMusclePart()
        musclePartViewModel.musclePartList.observe(this, androidx.lifecycle.Observer {
            for (musclePart in it) {
                val musclePartEntity =
                    MusclePart(musclePart.musclePartId, musclePart.musclePartName)
                musclePartViewModel.insertMusclePart(musclePartEntity)
            }
        })

        // トレーニングログ取得API
        logViewModel.getAllLog(userId)
        logViewModel.logList.observe(this, Observer {
            for (log in it) {
                val logEntity = Log(
                    log.logId,
                    log.menuId,
                    log.menuName,
                    log.trainingWeight,
                    log.trainingCount,
                    log.trainingDate,
                    log.trainingMemo
                )
                logViewModel.insertLogOfDB(logEntity)
            }
        })

        // 体組成データ取得API
        bodyCompViewModel.getAllBodyComp(userId)
        bodyCompViewModel.bodyCompList.observe(this, Observer {
            for (bodyComp in it) {
                val bodyCompEntity = BodyComp(
                    bodyComp.bodyCompId,
                    bodyComp.height,
                    bodyComp.weight,
                    bodyComp.bfp,
                    bodyComp.bmi,
                    bodyComp.lbm,
                    bodyComp.bodyCompDate
                )
                bodyCompViewModel.insertBodyCompDb(bodyCompEntity)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // ローカルDB全削除
        userViewModel.deleteAllUSerOfDb()
        bodyCompViewModel.deleteAllBodyCompDb()
        musclePartViewModel.deleteAllMusclePartOfDb()
        menuViewModel.deleteAllLogOfDb()
        logViewModel.deleteAllLogOfDb()
    }
}