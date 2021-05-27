package com.example.domain.usecases

import android.util.Log
import com.example.domain.models.Habit
import com.example.domain.repository.DatabaseRepository
import com.example.domain.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

class DownloadUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val remoteRepository: RemoteRepository
) {

    suspend fun download(callback: () -> Unit) {
        val habits = databaseRepository.getAllHabitsFromDB()
        try {
            var habitsFromServer = listOf<Habit>()
            try {
                habitsFromServer = remoteRepository.getHabits()
            } catch (e: HttpException) {
                Log.e(TAG, "download: ", e)
            }
            for (habit in habitsFromServer) {
                val habitFromDB = habits.find { it.serverId == habit.serverId }
                Log.i(TAG, "download: from server $habit")
                Log.i(TAG, "download: from DB $habitFromDB")
                if (habitFromDB != null) {
                    if (habit.date > habitFromDB.date) {
                        habit.id = habitFromDB.id
                        databaseRepository.save(habit)
                    }
                } else {
                    habit.id = UUID.randomUUID().toString()
                    databaseRepository.save(habit)

                }
            }
            val listNotInServer =
                habits.map { it.serverId }.minus(habitsFromServer.map { it.serverId })
            val deleted = mutableListOf<Habit>()
            for (deletedHabit in listNotInServer) {
                deleted.add(habits.find { it.serverId == deletedHabit }!!)
            }
            for (deletedHabit in deleted) {
                databaseRepository.delete(deletedHabit)
            }
        } catch (e: Exception) {
            Log.e(TAG, "download: $e", e)
        }

        withContext(Dispatchers.Main) {
            callback()
        }
    }

    companion object {
        private const val TAG = "DownloadUseCase"
    }
}