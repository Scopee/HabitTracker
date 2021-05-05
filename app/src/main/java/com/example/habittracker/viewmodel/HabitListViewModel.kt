package com.example.habittracker.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.habittracker.MainActivity
import com.example.habittracker.database.HabitsDatabase
import com.example.habittracker.models.Habit

class HabitListViewModel(activity: Activity) : ViewModel() {
    private val repository: HabitsRepository

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
        val database = HabitsDatabase.getDatabase(activity).habitDao()
        repository = HabitsRepository(database)
        repository.habits.observe (activity as MainActivity, Observer {
            list = it
            mutableLiveData.value = it
            updateList()
        })
    }

    private fun updateList() {
        var newList = list
        if (sortByName && sortByDate)
            newList = newList.sortedWith(compareBy({it.name}, {it.date}))
        else {
            if (sortByName)
                newList = newList.sortedBy { it.name }
            if (sortByDate)
                newList = newList.sortedBy { it.date }
        }
        newList = newList.filter { it.name.startsWith(nameFilter) }
        mutableLiveData.value = newList
    }

    companion object {
        private const val TAG = "HabitListViewModel"
    }
}