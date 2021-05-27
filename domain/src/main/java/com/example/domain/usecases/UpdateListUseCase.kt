package com.example.domain.usecases

import com.example.domain.models.Habit
import com.example.domain.repository.DatabaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateListUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    fun updateList(
        sortByNone: Boolean,
        sortByName: Boolean,
        sortByDate: Boolean,
        startsWith: String,
        dispatcher: CoroutineDispatcher
    ): Flow<List<Habit>> {
        if (sortByNone) {
            return databaseRepository.allHabits()
        }
        return databaseRepository.allHabits().map { sort(sortByName, sortByDate, startsWith, it) }
            .flowOn(dispatcher)
    }

    private fun sort(
        sortByName: Boolean,
        sortByDate: Boolean,
        startsWith: String,
        habits: List<Habit>
    ): List<Habit> {
        var newList = habits
        if (sortByName && sortByDate)
            newList = newList.sortedWith(compareBy({ it.name }, { it.date }))
        else {
            if (sortByName)
                newList = newList.sortedBy { it.name }
            if (sortByDate)
                newList = newList.sortedBy { it.date }
        }
        return newList.filter { it.name.startsWith(startsWith) }
    }
}