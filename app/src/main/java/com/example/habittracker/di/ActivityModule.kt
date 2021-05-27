package com.example.habittracker.di

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.MainActivity
import com.example.habittracker.viewmodel.HabitListViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {
    @Provides
    fun provideHabitListViewModel(activity: Activity): HabitListViewModel {
        return ViewModelProvider(
            (activity as MainActivity).mainFragment,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                    return HabitListViewModel(activity) as T
                }
            }).get(HabitListViewModel::class.java)
    }
}