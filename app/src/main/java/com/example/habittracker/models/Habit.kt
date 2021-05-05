package com.example.habittracker.models

import androidx.room.*
import java.time.LocalDateTime

@Entity(tableName = "habits")
@TypeConverters(
    Habit.PriorityConverter::class,
    Habit.ColorConverter::class,
    Habit.TypesConverter::class,
    Habit.LocalDateTimeConverter::class
)
data class Habit(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "priority") var priority: Priority,
    @ColumnInfo(name = "type") var type: Type,
    @ColumnInfo(name = "period") var period: String,
    @ColumnInfo(name = "color") var color: Color,
    @ColumnInfo(name = "updated") var date: LocalDateTime,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) {
    class PriorityConverter {
        @TypeConverter
        fun toPriority(value: String) = enumValueOf<Priority>(value)

        @TypeConverter
        fun fromPriority(value: Priority) = value.name
    }

    class ColorConverter {
        @TypeConverter
        fun toColor(value: String) = enumValueOf<Color>(value)

        @TypeConverter
        fun fromColor(value: Color) = value.name
    }

    class TypesConverter {
        @TypeConverter
        fun toColor(value: String) = enumValueOf<Type>(value)

        @TypeConverter
        fun fromColor(value: Type) = value.name
    }

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