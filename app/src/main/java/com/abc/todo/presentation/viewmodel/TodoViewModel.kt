package com.abc.todo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abc.todo.domain.model.Todo
import com.abc.todo.domain.usecase.AddTodosUseCase
import com.abc.todo.domain.usecase.DeleteTodosUseCase
import com.abc.todo.domain.usecase.GetTodosUseCase
import com.abc.todo.domain.usecase.UpdateTodosUseCase
import com.abc.todo.presentation.state.TodoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTodos: GetTodosUseCase,
    private val addTodo: AddTodosUseCase,
    private val updateTodo: UpdateTodosUseCase,
    private val deleteTodo: DeleteTodosUseCase
) : ViewModel() {

    // Filter state
    private val _isFiltered = MutableStateFlow(false)
    val isFiltered = _isFiltered.asStateFlow()

    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate = _endDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = combine(_isFiltered, _startDate, _endDate) { filtered, start, end ->
        Triple(filtered, start, end)
    }.flatMapLatest { (filtered, start, end) ->
        if (filtered) {
             if (start != null && end != null) {
                 getTodos(start, end)
             } else {
                 // Default to last month if filtered is on but no specific range is selected
                 // Or keep previous behavior if that's desired.
                 // For now, let's keep the "Last Month" logic as a fallback or default
                 val calendar = Calendar.getInstance()
                 calendar.add(Calendar.MONTH, -1)
                 getTodos(minDate = calendar.timeInMillis)
             }
        } else {
            getTodos()
        }
    }.map { TodoUiState.Success(it) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoUiState.Loading)


    fun toggleFilter() {
        _isFiltered.value = !_isFiltered.value
        // Reset custom dates when toggling off, or keep them?
        // Let's reset for clean state if toggling off
        if (!_isFiltered.value) {
            _startDate.value = null
            _endDate.value = null
        }
    }

    fun setDateRange(start: Long, end: Long) {
        _startDate.value = start
        _endDate.value = end
        if (!_isFiltered.value) {
            _isFiltered.value = true
        }
    }
    
    fun clearDateRange() {
         _startDate.value = null
         _endDate.value = null
    }


    fun add(title: String, description: String,date: Long) {
        viewModelScope.launch {
            addTodo(Todo(title=title,description=description, date = date, isCompleted = false))
        }
    }

    fun update(todo: Todo) {
        viewModelScope.launch {
            updateTodo(todo)
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch {
            deleteTodo(todo)
        }
    }

    fun toggle(todo: Todo) {
        viewModelScope.launch {
            updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }
}
