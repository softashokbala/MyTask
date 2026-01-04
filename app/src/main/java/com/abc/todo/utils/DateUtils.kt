package com.abc.todo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    fun Long.toFormattedDate(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date(this))
    }
    fun Long.toFormattedDateMonth(): String {
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        return formatter.format(Date(this))
    }

    fun Long.toFormattedDD(): String {
        val formatter = SimpleDateFormat("dd", Locale.getDefault())
        return formatter.format(Date(this))
    }

    fun Long.toFormattedMMM(): String {
        val formatter = SimpleDateFormat("MMM", Locale.getDefault())
        return formatter.format(Date(this))
    }


    fun formatDate(date: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(date))
    }
}