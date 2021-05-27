package com.example.domain.usecases

import com.example.domain.models.Habit
import com.example.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadAllHabitsUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    fun getAllHabits(): Flow<List<Habit>> {
        return databaseRepository.allHabits()
    }
}