package com.example.domain.usecases

import android.util.Log
import com.example.domain.models.Habit
import com.example.domain.models.HabitDone
import com.example.domain.models.Type
import com.example.domain.repository.DatabaseRepository
import com.example.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.DayOfWeek
import java.time.LocalDateTime
import javax.inject.Inject

class SetDoneUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val remoteRepository: RemoteRepository
) {

    suspend fun addDone(
        habit: Habit,
        dispatcher: CoroutineDispatcher,
        callback: (String) -> Unit
    ): String {
        val currentDate = LocalDateTime.now()
        habit.doneDates.add(currentDate)
        Log.i(TAG, "addDone: $habit")

        val startTime =
            currentDate.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val finishTime = startTime.plusDays(7)

        val currentDates = habit.doneDates.filter { inRange(startTime, finishTime, it) }
        val count = currentDates.count()

        when (habit.type) {
            Type.GOOD.ordinal -> {
                callback(
                    if (count < habit.period.toInt()) {
                        GOOD_NOT_DONE.format(habit.period.toInt() - count)
                    } else {
                        GOOD_DONE
                    }
                )
            }
            Type.BAD.ordinal -> {
                callback(
                    if (count < habit.period.toInt()) {
                        BAD_NOT_DONE.format(habit.period.toInt() - count)
                    } else {
                        BAD_DONE
                    }
                )
            }
        }
        databaseRepository.save(habit)
        withContext(dispatcher) {
            try {
                remoteRepository.habitDone(HabitDone(currentDate, habit.serverId))
            } catch (e: HttpException) {
                Log.e(TAG, "addDone: ", e)
            }
        }
        return ""
    }

    private fun inRange(start: LocalDateTime, finish: LocalDateTime, date: LocalDateTime): Boolean {
        return date.isAfter(start) && date.isBefore(finish)
    }

    companion object {
        private const val TAG = "SetDoneUseCase"
        private const val GOOD_NOT_DONE = "Стоит выполнить еще %d раз"
        private const val GOOD_DONE = "You're breathtaking!"

        private const val BAD_NOT_DONE = "Можете выполнить еще %d раз"
        private const val BAD_DONE = "Хватит это делать"
    }

}