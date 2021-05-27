package com.example.data.network.json

import com.example.domain.models.ServerUid
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ServerUidDeserializer : JsonDeserializer<ServerUid> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ServerUid =
        ServerUid(json.asJsonObject.get("uid").asString)
}