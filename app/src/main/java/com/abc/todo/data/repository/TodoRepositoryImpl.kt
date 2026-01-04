package com.abc.todo.data.repository

import com.abc.todo.data.mapper.toDomain
import com.abc.todo.data.mapper.toEntity
import com.abc.todo.data.mapper.toEntityForInsert
import com.abc.todo.data.mapper.toEntityForUpdate
import com.abc.todo.data.model.local.dao.TodoDao
import com.abc.todo.domain.model.Todo
import com.abc.todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(val todoDao: TodoDao): TodoRepository {

    override fun getAllTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTodosSince(minDate: Long): Flow<List<Todo>> {
        return todoDao.getTodosSince(minDate).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTodosBetween(startDate: Long, endDate: Long): Flow<List<Todo>> {
        return todoDao.getTodosBetween(startDate, endDate).map { list ->
            list.map { it.toDomain() }
        }
    }


    override suspend fun getTodoById(id: Long): Todo {
        todoDao.getTodoById(id).let {
            return it.toDomain()
        }
    }

    override suspend fun insertTodo(todo: Todo) {
        todoDao.insert(todo.toEntityForInsert())
    }


    override suspend fun updateTodo(todo: Todo) {
        val existing = todoDao.getTodoById(todo.id)
        todoDao.update(
            todo.toEntityForUpdate(
                id = existing.id,
                createdAt = existing.createdAt
            )
        )
    }

    override suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo.toEntity())
    }

}