package com.abc.todo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abc.todo.data.model.local.dao.TodoDao
import com.abc.todo.data.model.local.entity.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}