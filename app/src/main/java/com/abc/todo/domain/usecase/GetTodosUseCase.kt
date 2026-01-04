package com.abc.todo.domain.usecase

import com.abc.todo.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(val repository: TodoRepository) {
    operator fun invoke(minDate: Long? = null, maxDate: Long? = null): kotlinx.coroutines.flow.Flow<List<com.abc.todo.domain.model.Todo>> {
        return if (minDate != null && maxDate != null) {
            repository.getTodosBetween(minDate, maxDate)
        } else if (minDate != null) {
            repository.getTodosSince(minDate)
        } else {
            repository.getAllTodos()
        }
    }
}