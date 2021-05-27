package com.example.habittracker.viewmodel

import android.app.Activity
import androidx.lifecycle.*
import com.example.habittracker.MainActivity
import com.example.habittracker.models.PresentationHabit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitListViewModel @Inject constructor(private val activity: Activity) : ViewModel() {

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
            .asLiveData().observe(activity, Observer {
                list = it.map { habit -> PresentationHabit.fromDomainHabit(habit) }
                mutableLiveData.value = it.map { habit -> PresentationHabit.fromDomainHabit(habit) }
                    .sortedBy { it.name }
                updateList()
            })
        upload()
    }

    private fun updateList() {
        (activity as MainActivity).appComponent.getUpdateListUseCase()
            .updateList(sortByNone, sortByName, sortByDate, nameFilter, Dispatchers.Default)
            .asLiveData().observe(
                activity, Observer { it ->
                    mutableLiveData.value = it.map { PresentationHabit.fromDomainHabit(it) }
                })
    }

    fun upload() = (activity as MainActivity).lifecycleScope.launch {
        activity.appComponent.getUploadUseCase().upload()
    }

    fun download(callback: () -> Unit) = (activity as MainActivity).lifecycleScope.launch {
        activity.appComponent.getDownloadUseCase().download { callback() }
    }

    fun addDone(habit: PresentationHabit, callback: (String) -> Unit) =
        (activity as MainActivity).lifecycleScope.launch {
            activity.appComponent.getAddDoneUseCase()
                .addDone(habit.toDomainHabit(), Dispatchers.Default, callback)
        }

    companion object {
        private const val TAG = "HabitListViewModel"
    }
}