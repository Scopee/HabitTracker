package com.example.data.network.json

import android.util.Log
import com.example.domain.models.Habit
import com.google.gson.*
import java.lang.reflect.Type
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
        addProperty("color", src.color)
        addProperty("date", src.date.toEpochSecond(ZoneOffset.UTC))
        if (src.period.matches(Regex("\\d*")))
            addProperty("frequency", src.period.toInt())
        else
            addProperty("frequency", 0)
        if (src.priority != 3)
            addProperty("priority", src.priority)
        else
            addProperty("priority", 0)
        addProperty("type", src.type)
        addProperty("count", 0)
    }

    companion object {
        private const val TAG = "HabitSerializer"
    }

}