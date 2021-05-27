package com.example.domain.usecases;

import android.util.Log
import com.example.domain.repository.DatabaseRepository
import com.example.domain.repository.RemoteRepository
import javax.inject.Inject

class UploadUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val remoteRepository: RemoteRepository
) {

    suspend fun upload() {
        val habits = databaseRepository.getAllHabitsFromDB().filter {
            it.serverId.isEmpty()
        }
        for (habit in habits) {
            try {
                val response = remoteRepository.saveHabit(habit)
                habit.id = response.uid
                databaseRepository.save(habit)
            } catch (e: Exception) {
                Log.e(TAG, "upload: $e", e)
            }
        }
    }

    companion object {
        private const val TAG = "UploadUseCase"
    }
}
