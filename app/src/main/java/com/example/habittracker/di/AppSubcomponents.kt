package com.example.habittracker.di

import android.app.Activity
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ListHabitComponent::class, HabitComponent::class])
class AppSubcomponents(private val activity: Activity) {
    @Provides
    fun provideActivity() = activity
}