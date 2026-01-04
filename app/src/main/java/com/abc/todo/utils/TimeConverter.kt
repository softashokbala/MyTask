package com.abc.todo.utils

import androidx.room.TypeConverter
import java.time.Instant


class TimeConverter {

    @TypeConverter
    fun fromLong(value: Long?): Instant? =
        value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun toLong(instant: Instant?): Long? =
        instant?.toEpochMilli()
}
