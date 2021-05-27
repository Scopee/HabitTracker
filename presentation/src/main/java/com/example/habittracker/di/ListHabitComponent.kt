package com.example.habittracker.di

import android.app.Activity
import com.example.habittracker.ui.fragments.HabitListFragment
import com.example.habittracker.ui.fragments.MainFragment
import dagger.Subcomponent

@Subcomponent
interface ListHabitComponent {

    @Subcomponent.Builder
    interface Factory {
        fun create(): ListHabitComponent
    }

    fun inject(habitListFragment: HabitListFragment)

    fun inject(mainFragment: MainFragment)
}