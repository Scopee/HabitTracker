package com.example.habittracker.di

import android.app.Activity
import com.example.habittracker.ui.fragments.HabitListFragment
import com.example.habittracker.viewmodel.HabitListViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface HabitListSubComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun with(activity: Activity): Builder

        fun build(): HabitListSubComponent
    }

    fun inject(habitListFragment: HabitListFragment)
}