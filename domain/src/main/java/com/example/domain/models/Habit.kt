package com.example.domain.models

import androidx.room.*
import java.time.LocalDateTime

@Entity(tableName = "habits")
@TypeConverters(
    Habit.LocalDateTimeConverter::class
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
    @ColumnInfo(name = "server_id") var serverId: String = ""
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
}
