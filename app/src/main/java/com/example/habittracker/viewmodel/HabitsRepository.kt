package com.example.habittracker.viewmodel

import androidx.lifecycle.LiveData
import com.example.habittracker.database.dao.HabitDao
import com.example.habittracker.models.Habit

class HabitsRepository(private val habitDao: HabitDao) {
    val habits: LiveData<List<Habit>> = habitDao.getAllHabits()

    suspend fun delete(habit: Habit) {
        habitDao.delete(habit)
    }

    suspend fun save(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    fun getHabitById(id: String): LiveData<Habit> {
        return habitDao.getHabitById(id)
    }

    suspend fun getHabitByDatabaseId(id: String): Habit {
        return habitDao.getHabitByDatabaseId(id)
    }

    suspend fun getAllHabitsFromDB(): List<Habit> {
        return habitDao.getAllHabitsFromDB()
    }
}