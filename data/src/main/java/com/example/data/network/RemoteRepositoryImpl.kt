package com.example.data.network

import com.example.data.utils.retry
import com.example.domain.models.Habit
import com.example.domain.models.HabitDone
import com.example.domain.models.ServerUid
import com.example.domain.repository.RemoteRepository
import dagger.Component
import dagger.Provides
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val apiService: HabitApi) :
    RemoteRepository {

    override suspend fun getHabits(): List<Habit> = retry(times = 5) {
        apiService.getHabits()
    }


    override suspend fun saveHabit(habit: Habit): ServerUid =
        retry(times = 5) { apiService.saveHabit(habit) }


    override suspend fun deleteHabit(id: ServerUid) = retry(times = 5) {
        apiService.deleteHabit(id)
    }

    override suspend fun habitDone(habit: HabitDone) {
        apiService.habitDone(habit)
    }
}