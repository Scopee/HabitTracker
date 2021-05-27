package com.example.data.network.json


import com.example.domain.models.Habit
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset

class HabitDeserializer : JsonDeserializer<Habit> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Habit = Habit(
        json.asJsonObject.get("title").asString,
        json.asJsonObject.get("description").asString,
        json.asJsonObject.get("priority").asInt,
        json.asJsonObject.get("type").asInt,
        json.asJsonObject.get("frequency").asString,
        json.asJsonObject.get("color").asInt,
        LocalDateTime.ofEpochSecond(json.asJsonObject.get("date").asLong, 0, ZoneOffset.UTC),
        serverId = json.asJsonObject.get("uid").asString,
        doneDates = json.asJsonObject.get("done_dates").asJsonArray.map {
            LocalDateTime.ofEpochSecond(
                it.asLong,
                0,
                ZoneOffset.UTC
            )
        }.toMutableList()
    )
}