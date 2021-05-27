package com.example.domain.models

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.time.LocalDateTime
import java.time.ZoneOffset


@Entity(tableName = "habits")
@TypeConverters(
    Habit.LocalDateTimeConverter::class,
    Habit.ListConverter::class
)
data class Habit(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "priority") var priority: Int,
    @ColumnInfo(name = "type") var type: Int,
    @ColumnInfo(name = "period") var period: String,
    @ColumnInfo(name = "color") var color: Int,
    @ColumnInfo(name = "updated") var date: LocalDateTime,
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "server_id") var serverId: String = "",
    @ColumnInfo(name = "done_dates") var doneDates: MutableList<LocalDateTime> = mutableListOf()
) {
    class LocalDateTimeConverter {
        @TypeConverter
        fun toDate(value: String): LocalDateTime {
            return LocalDateTime.parse(value)
        }

        @TypeConverter
        fun fromDate(value: LocalDateTime): String {
            return value.toString()
        }
    }

    class ListConverter {

        var gson = Gson()

        @TypeConverter
        fun stringToList(data: String?): MutableList<LocalDateTime> {
            val parser = JsonParser()
            val obj = parser.parse(data)

            return obj.asJsonArray.map {
                LocalDateTime.ofEpochSecond(
                    it.asLong,
                    0,
                    ZoneOffset.UTC
                )
            }.toMutableList()
        }

        @TypeConverter
        fun listToString(someObjects: MutableList<LocalDateTime>): String? {
            return gson.toJson(someObjects.map { it.toEpochSecond(ZoneOffset.UTC) })
        }
    }

}
