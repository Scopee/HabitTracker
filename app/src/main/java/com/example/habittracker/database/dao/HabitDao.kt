package com.example.habittracker.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.habittracker.models.Habit

@Dao
interface HabitDao {
    @Query("select * from habits where id like :id")
    fun getHabitById(id: Int): Habit?

    @Update
    fun saveHabit(habit: Habit)

    @Insert
    fun insertHabit(habit: Habit)

    @Delete
    fun delete(habit: Habit)

    @Query("select * from habits")
    fun getAllHabits(): LiveData<List<Habit>>
}