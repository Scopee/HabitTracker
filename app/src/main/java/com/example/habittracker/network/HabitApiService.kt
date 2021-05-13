package com.example.habittracker.network

import com.example.habittracker.models.Habit
import com.example.habittracker.network.json.HabitDeserializer
import com.example.habittracker.network.json.HabitSerializer
import com.example.habittracker.network.json.ServerUidDeserializer
import com.example.habittracker.network.json.ServerUidSerializer
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HabitApiService {
    val serivce: HabitApi
    private val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                Habit::class.java,
                HabitSerializer()
            )
            .registerTypeAdapter(
                Habit::class.java,
                HabitDeserializer()
            )
            .registerTypeAdapter(
                ServerUid::class.java,
                ServerUidDeserializer()
            )
            .registerTypeAdapter(
                ServerUid::class.java,
                ServerUidSerializer()
            )
            .create()
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        serivce = retrofit.create(HabitApi::class.java)
    }

}