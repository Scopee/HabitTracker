package com.example.habittracker.models

import java.io.Serializable

class Habit(
    var id: Int,
    var name: String,
    var description: String,
    var priority: Priority,
    var type: Type,
    var period: String,
    var color: Color
) : Serializable{
    override fun toString(): String {
        return "Habit(id=$id, name='$name', description='$description', priority=$priority, type=$type, period='$period')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Habit

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (priority != other.priority) return false
        if (type != other.type) return false
        if (period != other.period) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + priority.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + period.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }


}