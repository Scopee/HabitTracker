
package com.example.habittracker.network.json

import com.example.habittracker.models.Color
import com.example.habittracker.models.Habit
import com.example.habittracker.models.Priority
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
    ): Habit = Habit (
        json.asJsonObject.get("title").asString,
        json.asJsonObject.get("description").asString,
        Priority.values()[json.asJsonObject.get("priority").asInt],
        com.example.habittracker.models.Type.values()[json.asJsonObject.get("type").asInt],
        json.asJsonObject.get("frequency").asString,
        Color.values()[json.asJsonObject.get("color").asInt],
        LocalDateTime.ofEpochSecond(json.asJsonObject.get("date").asLong, 0, ZoneOffset.UTC),
        serverId = json.asJsonObject.get("uid").asString
    )
}