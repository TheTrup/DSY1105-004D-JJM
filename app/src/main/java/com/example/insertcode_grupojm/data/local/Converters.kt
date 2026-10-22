package com.example.insertcode_grupojm.data.local

import androidx.room.TypeConverter
import java.util.Date

// TypeConverters para transformar tipos no nativos de SQLite a primitivos
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}