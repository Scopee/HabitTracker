package com.example.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.data.database.dao.HabitDao
import com.example.domain.models.Habit
import com.example.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepositoryImpl @Inject constructor(private val habitDao: HabitDao) :
    DatabaseRepository {
    val habits: Flow<List<Habit>> = habitDao.getAllHabits()

    override fun allHabits(): Flow<List<Habit>> = habits

    override suspend fun delete(habit: Habit) {
        habitDao.delete(habit)
    }

    override suspend fun save(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    override fun getHabitById(id: String): Flow<Habit> {
        return habitDao.getHabitById(id)
    }

    override suspend fun getHabitByDatabaseId(id: String): Habit {
        return habitDao.getHabitByDatabaseId(id)
    }

    override suspend fun getAllHabitsFromDB(): List<Habit> {
        return habitDao.getAllHabitsFromDB()
    }
}