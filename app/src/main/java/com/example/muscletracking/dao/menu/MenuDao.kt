package com.example.muscletracking.dao.menu

import androidx.room.*
import com.example.muscletracking.model.menu.Menu

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu")
    fun getAllMenu(): List<Menu>

    @Insert
    fun insertMenu(menu: Menu)

    @Update
    fun updateMenu(menu: Menu)

    @Delete
    fun deleteMenu(menu: Menu)
}