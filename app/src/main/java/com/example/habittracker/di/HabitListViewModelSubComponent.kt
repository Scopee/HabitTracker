package com.example.habittracker.di

import com.example.habittracker.viewmodel.HabitListViewModel
import dagger.Subcomponent

@Subcomponent
interface HabitListViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): HabitListViewModelSubComponent
    }

    fun inject(habitListViewModel: HabitListViewModel)
}