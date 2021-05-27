package com.example.data.network.json

import com.example.domain.models.HabitDone
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.ZoneOffset

class HabitDoneSerializer : JsonSerializer<HabitDone> {
    override fun serialize(
        src: HabitDone,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement = JsonObject().apply {
        addProperty("habit_uid",src.uid)
        addProperty("date", src.date.toEpochSecond(ZoneOffset.UTC))
    }

}
