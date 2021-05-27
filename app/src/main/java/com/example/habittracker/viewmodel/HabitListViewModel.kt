package com.example.habittracker.viewmodel

import android.app.Activity
import androidx.lifecycle.*
import com.example.habittracker.MainActivity
import com.example.habittracker.models.PresentationHabit
import kotlinx.coroutines.launch

class HabitListViewModel(private val activity: Activity) : ViewModel() {

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

    private val mutableLiveData: MutableLiveData<List<PresentationHabit>> = MutableLiveData()
    var sortedList: LiveData<List<PresentationHabit>> = mutableLiveData

    private var list = listOf<PresentationHabit>()

    init {
        (activity as MainActivity).appComponent.getLoadAllHabitsUseCase().getAllHabits()
            .asLiveData().observe(activity as MainActivity, Observer {
                list = it.map { habit -> PresentationHabit.fromDomainHabit(habit) }
                mutableLiveData.value = it.map { habit -> PresentationHabit.fromDomainHabit(habit) }
                updateList()
            })
        download {  }
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
        activity.appComponent.getUploadUseCase().upload()
    }

    fun download(callback: () -> Unit) = (activity as MainActivity).lifecycleScope.launch {
        activity.appComponent.getDownloadUseCase().download { callback() }
    }

    companion object {
        private const val TAG = "HabitListViewModel"
    }
}