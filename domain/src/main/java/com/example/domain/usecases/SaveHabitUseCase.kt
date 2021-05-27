package com.example.domain.usecases

import android.util.Log
import com.example.domain.models.Habit
import com.example.domain.models.ServerUid
import com.example.domain.repository.DatabaseRepository
import com.example.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject

class SaveHabitUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val remoteRepository: RemoteRepository
) {

    suspend fun saveHabit(habit: Habit, new: Boolean, dispatcher: CoroutineDispatcher) {
        Log.i(TAG, "saveHabit: $new")
        if (new)
            habit.id = UUID.randomUUID().toString()
        databaseRepository.save(habit)
        saveToServer(habit, new)
    }

    private suspend fun saveToServer(habit: Habit, new: Boolean) {
        try {
            if (new) {
                val response = remoteRepository.saveHabit(habit)
                habit.serverId = response.uid
                databaseRepository.save(habit)
            } else {
                remoteRepository.saveHabit(habit)
            }
        } catch (e: Exception) {
            Log.e(TAG, "saveHabit: $e")
        }
    }

    companion object {
        private const val TAG = "saveHabit"
    }
}