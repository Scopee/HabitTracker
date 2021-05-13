package com.example.habittracker.network.json

import com.example.habittracker.models.Habit
import com.example.habittracker.models.Priority
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset

class HabitSerializer : JsonSerializer<Habit> {
    override fun serialize(
        src: Habit,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement = JsonObject().apply {
        if (src.id.isNotEmpty())
            addProperty("uid", src.serverId)
        addProperty("title", src.name)
        addProperty("description", src.description)
        addProperty("color", src.color.ordinal)
        addProperty("date", src.date.toEpochSecond(ZoneOffset.UTC))
        if (src.period.matches(Regex("\\d*")))
            addProperty("frequency", src.period.toInt())
        else
            addProperty("frequency", 0)
        if (src.priority != Priority.NONE)
            addProperty("priority", src.priority.ordinal)
        else
            addProperty("priority", 0)
        addProperty("type", src.type.ordinal)
        addProperty("count", 0)
    }
}