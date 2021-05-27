package com.example.data.di

import android.content.Context
import com.example.data.database.DatabaseRepositoryImpl
import com.example.data.database.HabitsDatabase
import com.example.data.database.dao.HabitDao
import com.example.data.network.HabitApi
import com.example.data.network.RemoteRepositoryImpl
import com.example.data.network.json.HabitDeserializer
import com.example.data.network.json.HabitDoneSerializer
import com.example.data.network.json.HabitSerializer
import com.example.data.network.json.ServerUidDeserializer
import com.example.domain.models.Habit
import com.example.domain.models.HabitDone
import com.example.domain.models.ServerUid
import com.example.domain.repository.DatabaseRepository
import com.example.domain.repository.RemoteRepository
import com.example.habittracker.network.json.ServerUidSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ConnectionsModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideRemoteRepository(habitApi: HabitApi) : RemoteRepository {
        return RemoteRepositoryImpl(habitApi)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(habitDao: HabitDao) : DatabaseRepository {
        return DatabaseRepositoryImpl(habitDao)
    }

    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun provideDao() : HabitDao {
        return HabitsDatabase.getDatabase(context).habitDao()
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit) : HabitApi {
        return retrofit.create(HabitApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    fun provideClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    @Provides
    fun provideGson() : Gson {
        return GsonBuilder()
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
            .registerTypeAdapter(
                HabitDone::class.java,
                HabitDoneSerializer()
            )
            .create()
    }

    companion object {
        private const val BASE_URL =  "https://droid-test-server.doubletapp.ru/api/"
    }
}