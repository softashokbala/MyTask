package com.abc.todo.domain.repository

import com.abc.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getAllTodos(): Flow<List<Todo>>

    fun getTodosSince(minDate: Long): Flow<List<Todo>>

    fun getTodosBetween(startDate: Long, endDate: Long): Flow<List<Todo>>

    suspend fun getTodoById(id: Long): Todo?

    suspend fun insertTodo(todo: Todo)

    suspend fun updateTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)
}