package com.example.habittracker.models

import com.example.domain.models.Habit
import java.time.LocalDateTime


data class PresentationHabit(
    var name: String,
    var description: String,
    var priority: Priority,
    var type: Type,
    var period: String,
    var color: Color,
    var date: LocalDateTime,
    var id: String = "",
    var serverId: String = "",
    val doneDates: MutableList<LocalDateTime> = mutableListOf()
) {

    fun toDomainHabit(): Habit {
        return Habit(
            name,
            description,
            priority.ordinal,
            type.ordinal,
            period,
            color.ordinal,
            date,
            id,
            serverId,
            doneDates
        )
    }

    companion object {
        fun fromDomainHabit(habit: Habit): PresentationHabit {
            return PresentationHabit(
                habit.name,
                habit.description,
                Priority.values()[habit.priority],
                Type.values()[habit.type],
                habit.period,
                Color.values()[habit.color],
                habit.date,
                habit.id,
                habit.serverId,
                habit.doneDates
            )
        }
    }
}

