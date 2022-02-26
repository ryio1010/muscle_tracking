package com.example.muscletracking.config.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.muscletracking.dao.menu.MenuDao
import com.example.muscletracking.dao.musclepart.MusclePartDao
import com.example.muscletracking.dao.user.UserDao
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.model.musclepart.MusclePart
import com.example.muscletracking.model.user.User

@Database(
    entities = [User::class, Menu::class, MusclePart::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun menuDao(): MenuDao
    abstract fun musclePartDao(): MusclePartDao

    companion object {
        private const val dbName = "mt_db"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            dbName
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return requireNotNull(instance)
        }
    }
}