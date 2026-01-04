package com.abc.todo.presentation.state

import com.abc.todo.domain.model.Todo

sealed class TodoUiState {
    object Loading : TodoUiState()
    data class Success(val todos: List<Todo>) : TodoUiState()
    data class Error(val message: String) : TodoUiState()
}
