package com.example.habittracker.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.habittracker.MainActivity
import com.example.habittracker.database.HabitsDatabase
import com.example.habittracker.models.*
import com.example.habittracker.network.HabitApiService
import com.example.habittracker.network.HabitsRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class HabitViewModel(val activity: Activity, private val id: String) : ViewModel() {
    private val habitsRepository: HabitsRepository
    private val remoteRepository: HabitsRemoteRepository

    private val mutableHabit: MutableLiveData<Habit> = MutableLiveData()

    val habit: LiveData<Habit> = mutableHabit

    var good = false
        set(value) {
            field = value
            habit.value?.type = Type.GOOD
        }
        get() = habit.value?.type == Type.GOOD
    var bad = false
        set(value) {
            field = value
            habit.value?.type = Type.BAD
        }
        get() = habit.value?.type == Type.BAD

    var red = false
        set(value) {
            field = value
            habit.value?.color = Color.RED
        }
        get() = habit.value?.color == Color.RED
    var green = false
        set(value) {
            field = value
            habit.value?.color = Color.GREEN
        }
        get() = habit.value?.color == Color.GREEN
    var blue = false
        set(value) {
            field = value
            habit.value?.color = Color.BLUE
        }
        get() = habit.value?.color == Color.BLUE

    init {
        Log.i(TAG, "id= $id")
        val habitDao = HabitsDatabase.getDatabase(activity).habitDao()
        val apiService = HabitApiService.serivce
        habitsRepository = HabitsRepository(habitDao)
        remoteRepository = HabitsRemoteRepository(apiService)
        habitsRepository.getHabitById(id).observe(activity as MainActivity, Observer {
            if (it == null) {
                mutableHabit.value = Habit(
                    "",
                    "",
                    Priority.NONE,
                    Type.GOOD,
                    "",
                    Color.WHITE,
                    LocalDateTime.now()
                )
            } else {
                mutableHabit.value = it
            }
            Log.i(TAG, "observe: ${mutableHabit.value}")
        })
    }

    fun saveHabit(habit: Habit, new: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "saveHabit: $new")
        if (new)
            habit.id = UUID.randomUUID().toString()
        saveToDataBase(habit)
        saveToServer(habit, new)
    }

    private fun saveToDataBase(habit: Habit) = viewModelScope.launch {
        habitsRepository.save(habit)
    }

    private fun saveToServer(habit: Habit, new: Boolean) =
        (activity as MainActivity).lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (new) {
                    val response = remoteRepository.saveHabit(habit)
                    habit.serverId = response.uid
                    habitsRepository.save(habit)
                } else {
                    remoteRepository.saveHabit(habit)
                }
            } catch (e: Exception) {
                Log.e(TAG, "saveHabit: $e")
            }
        }

    fun isEmpty(): Boolean {
        return habit.value!!.name.isEmpty() || habit.value!!.description.isEmpty()
    }

    companion object {
        private const val TAG = "HabitViewModel"
    }
}