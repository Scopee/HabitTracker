package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun allHabits(): Flow<List<Habit>>

    suspend fun delete(habit: Habit)

    suspend fun save(habit: Habit)

    fun getHabitById(id: String): Flow<Habit>

    suspend fun getHabitByDatabaseId(id: String): Habit

    suspend fun getAllHabitsFromDB(): List<Habit>
}