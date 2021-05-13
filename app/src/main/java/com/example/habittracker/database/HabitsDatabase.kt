package com.example.habittracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habittracker.database.dao.HabitDao
import com.example.habittracker.models.Habit

@Database(entities = [Habit::class], version = 1)
abstract class HabitsDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: HabitsDatabase? = null

        fun getDatabase(context: Context): HabitsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitsDatabase::class.java,
                    "habits_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}