package com.example.muscletracking.dao.menu

import androidx.room.*
import com.example.muscletracking.model.menu.Menu

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu")
    fun getAllMenu(): List<Menu>

    @Query("SELECT * FROM menu WHERE musclePart = :musclePart")
    fun getMenuByMusclePart(musclePart: String): List<Menu>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenu(menu: Menu)

    @Update
    fun updateMenu(menu: Menu)

    @Delete
    fun deleteMenu(menu: Menu)

    @Query("DELETE FROM menu")
    fun deleteAllMenu()
}