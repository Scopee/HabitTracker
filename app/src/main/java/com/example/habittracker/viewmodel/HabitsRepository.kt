package com.example.habittracker.viewmodel

import androidx.lifecycle.LiveData
import com.example.habittracker.database.dao.HabitDao
import com.example.habittracker.models.Habit

class HabitsRepository(private val habitDao: HabitDao) {
    val habits: LiveData<List<Habit>> = habitDao.getAllHabits()

    fun delete(habit: Habit) {
        habitDao.delete(habit)
    }

    fun save(habit: Habit) {
        if (habitDao.getHabitById(habit.id) != null) {
            habitDao.saveHabit(habit)
        } else {
            habitDao.insertHabit(habit)
        }
    }

    fun getHabitById(id: Int): Habit? {
        return habitDao.getHabitById(id)
    }
}