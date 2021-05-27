package com.example.habittracker.di

import com.example.habittracker.ui.fragments.HabitFragment
import dagger.Subcomponent

@Subcomponent
interface HabitComponent {
    @Subcomponent.Builder
    interface Factory {
        fun create(): HabitComponent
    }

    fun inject(habitFragment: HabitFragment)
}