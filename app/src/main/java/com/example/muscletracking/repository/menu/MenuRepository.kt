package com.example.muscletracking.repository.menu

import android.app.Application
import androidx.annotation.WorkerThread
import com.example.muscletracking.config.api.RetrofitClient
import com.example.muscletracking.config.db.AppDatabase
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.model.menu.MenuResponse

class MenuRepository(app:Application) {

    private val menuDao = AppDatabase.getInstance(app).menuDao()
    private val retrofitClient = RetrofitClient.getApiService()

    /**
     * ローカルDB全件検索
     */
    @WorkerThread
    suspend fun getAllMenuFromDB() :List<Menu> {
        return menuDao.getAllMenu()
    }

    /**
     * ローカルDB登録処理
     */
    @WorkerThread
    suspend fun insertMenu(menu:Menu) {
        return menuDao.insertMenu(menu)
    }


    /**
     * トレーニングメニュー取得API
     */
    @WorkerThread
    suspend fun getAllMenu(userId:String):List<MenuResponse>? {
        val response = retrofitClient.getAllMenu(userId).execute()
        return if (response.isSuccessful) {
            return response.body()
        }else {
            null
        }
    }
}