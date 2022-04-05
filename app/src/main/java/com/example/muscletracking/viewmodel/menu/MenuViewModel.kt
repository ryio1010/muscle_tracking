package com.example.muscletracking.viewmodel.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.model.menu.MenuResponse
import com.example.muscletracking.repository.menu.MenuRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MenuViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: MenuRepository = MenuRepository(app)

    val menuList: MutableLiveData<List<MenuResponse>> = MutableLiveData()
    val addedMenuList: MutableLiveData<List<MenuResponse>> = MutableLiveData()
    val menuListOfDB: MutableLiveData<List<Menu>> = MutableLiveData()
    val menuListByPartOfDB: MutableLiveData<List<Menu>> = MutableLiveData()

    // coroutine用
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    /**
     * トレーニングメニュー全件取得（ローカルDB）
     */
    fun getAllMenuFromDB() = scope.launch(Dispatchers.IO) {
        val allMenu = repository.getAllMenuFromDB()
        menuListOfDB.postValue(allMenu)
    }

    /**
     * トレーニングメニュー取得（ローカルDB）
     * 引数：MusclePart
     */
    fun getAllMenuByMusclePartFromDB(musclePart: String) = scope.launch(Dispatchers.IO) {
        val allMenu = repository.getMenuFromDBByMusclePart(musclePart)
        menuListByPartOfDB.postValue(allMenu)
    }


    /**
     * トレーニングメニューInsert（ローカルDB）
     */
    fun insertMenu(menu: Menu) = scope.launch(Dispatchers.IO) {
        repository.insertMenu(menu)
    }

    fun deleteAllLogOfDb() = scope.launch(Dispatchers.IO) {
        repository.deleteAllMenuOfDb()
    }

    /**
     * トレーニングメニュー取得API実行
     */
    fun getAllMenu(userId: String) = scope.launch(Dispatchers.IO) {
        val allMenu = repository.getAllMenu(userId)
        if (allMenu == null) {
            menuList.postValue(null)
        } else {
            menuList.postValue(allMenu)
        }
    }

    /**
     * トレーニングメニュー追加API実行
     */
    fun addMenu(musclePartId: String, menuName: String, userId: String) =
        scope.launch(Dispatchers.IO) {
            val allMenu = repository.addMenu(musclePartId, menuName, userId)
            if (allMenu == null) {
                addedMenuList.postValue(null)
            } else {
                addedMenuList.postValue(allMenu)
            }
        }
}