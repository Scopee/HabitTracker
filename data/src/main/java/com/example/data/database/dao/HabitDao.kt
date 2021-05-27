package com.example.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("select * from habits where id like :id")
    fun getHabitById(id: String): Flow<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("select * from habits")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("select * from habits where id like :id")
    suspend fun getHabitByDatabaseId(id: String): Habit

    @Query("select * from habits")
    suspend fun getAllHabitsFromDB(): List<Habit>
}