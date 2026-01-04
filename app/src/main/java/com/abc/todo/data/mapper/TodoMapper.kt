package com.abc.todo.data.mapper

import com.abc.todo.data.model.local.entity.TodoEntity
import com.abc.todo.domain.model.Todo


fun TodoEntity.toDomain() = Todo(id, title, description, date,isCompleted)

fun Todo.toEntity() = TodoEntity(id, title, description, date,isCompleted, updatedAt = System.currentTimeMillis())

fun Todo.toEntityForInsert(): TodoEntity =
    TodoEntity(
        title = title,
        description = description,
        isCompleted = isCompleted,
        date = date,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )

fun Todo.toEntityForUpdate(id:Long, createdAt: Long): TodoEntity =
    TodoEntity(
        id = id,
        title = title,
        description = description,
        date = date,
        isCompleted = isCompleted,
        createdAt = createdAt,
        updatedAt = System.currentTimeMillis()
    )
