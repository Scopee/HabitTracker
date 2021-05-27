package com.example.habittracker.network.json

import com.example.domain.models.ServerUid
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ServerUidSerializer : JsonSerializer<ServerUid> {
    override fun serialize(
        src: ServerUid,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement = JsonObject().apply {
        addProperty("uid", src.uid)
    }
}