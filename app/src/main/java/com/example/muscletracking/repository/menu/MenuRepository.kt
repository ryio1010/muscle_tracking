package com.example.muscletracking.repository.menu

import android.app.Application
import androidx.annotation.WorkerThread
import com.example.muscletracking.config.api.RetrofitClient
import com.example.muscletracking.config.db.AppDatabase
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.model.menu.MenuResponse

class MenuRepository(app: Application) {

    private val menuDao = AppDatabase.getInstance(app).menuDao()
    private val retrofitClient = RetrofitClient.getApiService()

    /**
     * ローカルDB全件検索
     */
    @WorkerThread
    suspend fun getAllMenuFromDB(): List<Menu> {
        return menuDao.getAllMenu()
    }

    /**
     * ローカルDB検索（引数：MusclePart）
     */
    @WorkerThread
    suspend fun getMenuFromDBByMusclePart(musclePart: String): List<Menu> {
        return menuDao.getMenuByMusclePart(musclePart)
    }

    /**
     * ローカルDB登録処理
     */
    @WorkerThread
    suspend fun insertMenu(menu: Menu) {
        return menuDao.insertMenu(menu)
    }

    @WorkerThread
    suspend fun deleteAllMenuOfDb() {
        return menuDao.deleteAllMenu()
    }


    /**
     * トレーニングメニュー取得API
     */
    @WorkerThread
    suspend fun getAllMenu(userId: String): List<MenuResponse>? {
        val response = retrofitClient.getAllMenu(userId).execute()
        return if (response.isSuccessful) {
            return response.body()
        } else {
            null
        }
    }

    /**
     * トレーニングメニュー追加API
     */
    @WorkerThread
    suspend fun addMenu(
        musclePartId: String,
        menuName: String,
        userId: String
    ): List<MenuResponse>? {
        val response = retrofitClient.addMenu(musclePartId, menuName, userId).execute()
        return if (response.isSuccessful) {
            return response.body()
        } else {
            null
        }
    }
}