package com.example.habittracker.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.habittracker.models.Habit

@Dao
interface HabitDao {
    @Query("select * from habits where id like :id")
    fun getHabitById(id: Int): LiveData<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("select * from habits")
    fun getAllHabits(): LiveData<List<Habit>>
}