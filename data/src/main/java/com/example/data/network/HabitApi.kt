package com.example.data.network

import com.example.domain.models.Habit
import com.example.domain.models.ServerUid
import retrofit2.http.*

interface HabitApi {
    @Headers("Authorization:ae1fc5b5-34ac-44c4-9b55-4ff9879bf749")
    @GET("habit")
    suspend fun getHabits(): List<Habit>

    @Headers("Authorization:ae1fc5b5-34ac-44c4-9b55-4ff9879bf749")
    @PUT("habit")
    suspend fun saveHabit(@Body habit: Habit) : ServerUid

    @Headers("Authorization:ae1fc5b5-34ac-44c4-9b55-4ff9879bf749")
    @HTTP(method = "DELETE", hasBody = true, path = "habit")
    suspend fun deleteHabit(@Body uid: ServerUid)
}