package com.example.habittracker.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.habittracker.MainActivity
import com.example.habittracker.database.HabitsDatabase
import com.example.habittracker.models.Habit
import com.example.habittracker.network.HabitApiService
import com.example.habittracker.network.HabitsRemoteRepository
import com.example.habittracker.network.ServerUid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HabitListViewModel(val activity: Activity) : ViewModel() {
    private val habitsRepository: HabitsRepository
    private val remoteRepository: HabitsRemoteRepository

    var sortByNone: Boolean = true
        set(value) {
            field = value
            if (value) {
                sortByName = false
                sortByDate = false
            }
            updateList()
        }
    var sortByName: Boolean = false
        set(value) {
            field = value
            if (value) {
                sortByNone = false
            }
            updateList()
        }
    var sortByDate: Boolean = false
        set(value) {
            field = value
            if (value) {
                sortByNone = false
            }
            updateList()
        }
    var nameFilter: String = ""
        set(value) {
            field = value
            updateList()
        }

    private val mutableLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
    var sortedList: LiveData<List<Habit>> = mutableLiveData

    private var list = listOf<Habit>()

    init {
        val habitDao = HabitsDatabase.getDatabase(activity).habitDao()
        val apiService = HabitApiService.serivce
        habitsRepository = HabitsRepository(habitDao)
        remoteRepository = HabitsRemoteRepository(apiService)
        habitsRepository.habits.observe(activity as MainActivity, Observer {
            list = it
            mutableLiveData.value = it
            updateList()
        })
        upload()
//        deleteAll()
    }

    private fun updateList() {
        var newList = list
        if (sortByName && sortByDate)
            newList = newList.sortedWith(compareBy({ it.name }, { it.date }))
        else {
            if (sortByName)
                newList = newList.sortedBy { it.name }
            if (sortByDate)
                newList = newList.sortedBy { it.date }
        }
        newList = newList.filter { it.name.startsWith(nameFilter) }
        mutableLiveData.value = newList
    }

    fun upload() = (activity as MainActivity).lifecycleScope.launch {
        val habits = habitsRepository.getAllHabitsFromDB().filter { it.serverId.isEmpty() }
        for (habit in habits) {
            try {
                val response = remoteRepository.saveHabit(habit)
                habit.id = response.uid
                habitsRepository.save(habit)
            } catch (e: Exception) {
                Log.e(TAG, "upload: $e", e)
            }
        }
    }

    fun download(callback: () -> Unit) = (activity as MainActivity).lifecycleScope.launch {
        val habits = habitsRepository.getAllHabitsFromDB()
        try {
            val habitsFromServer = remoteRepository.getHabits()
            for (habit in habitsFromServer) {
                val habitFromDB = habits.find { it.serverId == habit.serverId }
                Log.i(TAG, "download: from server $habit")
                Log.i(TAG, "download: from DB $habitFromDB")
                if (habitFromDB != null) {
                    if (habit.date > habitFromDB.date) {
                        habit.id = habitFromDB.id
                        habitsRepository.save(habit)
                    }
                } else {
                    habit.id = UUID.randomUUID().toString()
                    habitsRepository.save(habit)

                }
            }
            val listNotInServer =
                habits.map { it.serverId }.minus(habitsFromServer.map { it.serverId })
            val deleted = mutableListOf<Habit>()
            for (deletedHabit in listNotInServer) {
                deleted.add(habits.find { it.serverId == deletedHabit }!!)
            }
            for (deletedHabit in deleted) {
                habitsRepository.delete(deletedHabit)
            }
        } catch (e: Exception) {
            Log.e(TAG, "download: $e", e)
        }

        withContext(Dispatchers.Main) {
            callback()
        }
    }

    private fun deleteAll() = (activity as MainActivity).lifecycleScope.launch {
        val habitsFromServer = remoteRepository.getHabits().map { it.serverId }
        for (id in habitsFromServer) {
            remoteRepository.deleteHabit(ServerUid(id))
        }
    }

    companion object {
        private const val TAG = "HabitListViewModel"
    }
}