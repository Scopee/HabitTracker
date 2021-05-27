package com.example.habittracker.di

import androidx.room.Update
import com.example.data.di.ConnectionsModule
import com.example.domain.usecases.*
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ConnectionsModule::class, AppSubcomponents::class])
@Singleton
interface AppComponent {
    fun getDownloadUseCase(): DownloadUseCase

    fun getSaveHabitUseCase(): SaveHabitUseCase

    fun getUploadUseCase(): UploadUseCase

    fun getGetHabitUseCase(): GetHabitUseCase

    fun getLoadAllHabitsUseCase(): LoadAllHabitsUseCase

    fun getAddDoneUseCase(): SetDoneUseCase

    fun getListHabitComponent(): ListHabitComponent.Factory

    fun getHabitComponent() : HabitComponent.Factory

    fun getUpdateListUseCase() : UpdateListUseCase
}