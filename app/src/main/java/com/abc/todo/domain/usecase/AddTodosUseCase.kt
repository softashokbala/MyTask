package com.abc.todo.domain.usecase

import com.abc.todo.domain.model.Todo
import com.abc.todo.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodosUseCase @Inject constructor(val repository: TodoRepository) {

    suspend operator fun invoke(todo: Todo) {
        repository.insertTodo(todo)
    }
}