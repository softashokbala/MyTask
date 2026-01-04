package com.abc.todo.data.model.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.abc.todo.data.model.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao{

    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE date >= :minDate")
    fun getTodosSince(minDate: Long): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE date BETWEEN :startDate AND :endDate")
    fun getTodosBetween(startDate: Long, endDate: Long): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Long): TodoEntity

    @Insert
    suspend fun insert(todo: TodoEntity): Long

    @Update
    suspend fun update(todo: TodoEntity)

    // Optional: know how many rows updated
    @Update
    suspend fun updateTodoCount(todo: TodoEntity): Int



    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

}