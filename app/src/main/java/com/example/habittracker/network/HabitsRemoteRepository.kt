package com.example.habittracker.network

import com.example.habittracker.models.Habit
import com.example.habittracker.util.retry

class HabitsRemoteRepository(private val api: HabitApi) {
    suspend fun getHabits(): List<Habit> {
        return retry(times = 5) {
            api.getHabits()
        }
    }

    suspend fun saveHabit(habit: Habit): ServerUid {
        return retry(times = 5) {
            api.saveHabit(habit)
        }
    }


    suspend fun deleteHabit(id: ServerUid) {
        retry(times = 5) { api.deleteHabit(id) }
    }
}