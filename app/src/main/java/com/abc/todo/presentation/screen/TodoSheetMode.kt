package com.abc.todo.presentation.screen

import com.abc.todo.domain.model.Todo

sealed class TodoSheetMode {
    object Add : TodoSheetMode()
    data class Edit(val todo: Todo) : TodoSheetMode()
}
