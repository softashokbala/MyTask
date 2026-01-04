package com.abc.todo.domain.model

data class Todo(
    val id: Long=0L,
    val title: String,
    val description: String,
    val date: Long,
    val isCompleted: Boolean,
)

//val date: Long = System.currentTimeMillis(),