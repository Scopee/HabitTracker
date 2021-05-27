package com.example.domain.repository

import com.example.domain.models.Habit
import com.example.domain.models.HabitDone
import com.example.domain.models.ServerUid

interface RemoteRepository {

    suspend fun getHabits(): List<Habit>
    

    suspend fun saveHabit(habit: Habit): ServerUid


    suspend fun deleteHabit(id: ServerUid)

    suspend fun habitDone(habit: HabitDone)
}