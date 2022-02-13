package com.example.muscletracking

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.muscletracking.dao.UserDao
import com.example.muscletracking.model.user.User

@Database(entities = [User::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

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