package com.example.muscletracking.dao.bodycomp

import androidx.room.*
import com.example.muscletracking.model.bodycomp.BodyComp

@Dao
interface BodyCompDao {

    @Query("SELECT * FROM bodycomp order by date desc limit 1")
    fun getLatestBodyComp(): BodyComp

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bodyComp: BodyComp)

    @Update
    fun update(bodyComp: BodyComp)

    @Query("DELETE FROM bodycomp")
    fun deleteAll()
}