package com.example.habittracker.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.MainActivity
import com.example.habittracker.models.Color
import com.example.habittracker.models.PresentationHabit
import com.example.habittracker.models.Priority
import com.example.habittracker.models.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class HabitViewModel @Inject constructor(val activity: Activity) : ViewModel() {
    private val mutableHabit: MutableLiveData<PresentationHabit> = MutableLiveData()

    val habit: LiveData<PresentationHabit> = mutableHabit

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

    fun init(id: String) {
        Log.i(TAG, "id= $id")
        (activity as MainActivity).appComponent.getGetHabitUseCase().getHabit(id).asLiveData()
            .observe(activity, Observer {
                if (it == null) {
                    mutableHabit.value = PresentationHabit(
                        "",
                        "",
                        Priority.NONE,
                        Type.GOOD,
                        "",
                        Color.WHITE,
                        LocalDateTime.now()
                    )
                } else {
                    mutableHabit.value = PresentationHabit.fromDomainHabit(it)
                }
                Log.i(TAG, "observe: ${mutableHabit.value}")
            })
    }

    fun saveHabit(habit: PresentationHabit, new: Boolean) =
        viewModelScope.launch(Dispatchers.Main) {
            (activity as MainActivity).appComponent.getSaveHabitUseCase()
                .saveHabit(habit.toDomainHabit(), new, Dispatchers.Default)
        }

    fun isEmpty(): Boolean {
        return habit.value!!.name.isEmpty() || habit.value!!.description.isEmpty()
    }

    companion object {
        private const val TAG = "HabitViewModel"
    }
}