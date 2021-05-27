package com.example.domain.usecases

import com.example.domain.models.Habit
import com.example.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    fun getHabit(id: String): Flow<Habit> {
        return databaseRepository.getHabitById(id)
    }
}