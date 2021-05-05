package com.example.habittracker.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.database.HabitsDatabase
import com.example.habittracker.models.Color
import com.example.habittracker.models.Habit
import com.example.habittracker.models.Priority
import com.example.habittracker.models.Type
import java.time.LocalDateTime

class HabitViewModel(context: Context, private val id: Int) : ViewModel() {
    private val repository: HabitsRepository

    private val mutableHabit: MutableLiveData<Habit> = MutableLiveData()

    val habit: LiveData<Habit> = mutableHabit

    var good = false
        set(value) {
            field = value
            if (value) {
                habit.value?.type = Type.GOOD
                bad = false
            }
        }
    var bad = false
        set(value) {
            field = value
            if (value) {
                habit.value?.type = Type.BAD
                good = false
            }
        }

    var red = false
        set(value) {
            field = value
            if (value) {
                habit.value?.color = Color.RED
                green = false
                blue = false
            }
        }
    var green = false
        set(value) {
            field = value
            if (value) {
                habit.value?.color = Color.GREEN
                red = false
                blue = false
            }
        }
    var blue = false
        set(value) {
            field = value
            if (value) {
                habit.value?.color = Color.BLUE
                red = false
                green = false
            }
        }

    init {
        Log.i(TAG, "id= $id")
        val habitDao = HabitsDatabase.getDatabase(context).habitDao()
        repository = HabitsRepository(habitDao)
        var habitFromDatabase = repository.getHabitById(id)
        Log.i(TAG, "habit=$habitFromDatabase ")
        if (habitFromDatabase == null) {
            habitFromDatabase = Habit(
                "",
                "",
                Priority.NONE,
                Type.GOOD,
                "",
                Color.WHITE,
                LocalDateTime.now()
            )
        }
        mutableHabit.value = habitFromDatabase

        initCheckboxes()
        Log.i(TAG, "INIT: ${mutableHabit.value}")
    }

    fun saveHabit(habit: Habit) {
        repository.save(habit)
    }

    fun isEmpty(): Boolean {
        return habit.value!!.name.isEmpty() || habit.value!!.description.isEmpty()
    }

    private fun initCheckboxes() {
        if (mutableHabit.value!!.type == Type.GOOD) {
            good = true
        } else {
            bad = true
        }
        when (mutableHabit.value!!.color) {
            Color.BLUE -> blue = true
            Color.GREEN -> green = true
            Color.RED -> red = true
            else -> red = false
        }
    }

    companion object {
        private const val TAG = "HabitViewModel"
    }
}