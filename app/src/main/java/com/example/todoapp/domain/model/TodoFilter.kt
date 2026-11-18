package com.example.todoapp.domain.model

import java.util.Calendar

enum class TimeFilter { ALL, TODAY, UPCOMING, OVERDUE }

enum class StatusFilter { ALL, ACTIVE, COMPLETED }

data class TodoFilter(
    val time: TimeFilter = TimeFilter.ALL,
    val priority: Priority? = null,      // null = sab priorities
    val query: String = ""
)

fun List<Todo>.applyFilter(criteria: TodoFilter, now: Long = System.currentTimeMillis()): List<Todo> {
    val todayStart = startOfDay(now)
    val tomorrowStart = startOfDay(now, 1)
    val q = criteria.query.trim()
    return filter { todo ->
        val due = todo.dueDate
        val queryOk = q.isEmpty() || todo.title.contains(q, ignoreCase = true)
        val priorityOk = criteria.priority == null || todo.priority == criteria.priority
        val timeOk = when (criteria.time) {
            TimeFilter.ALL -> true
            TimeFilter.TODAY -> due != null && due >= todayStart && due < tomorrowStart
            TimeFilter.UPCOMING -> due != null && due >= tomorrowStart
            TimeFilter.OVERDUE -> todo.isOverdue(now)
        }
        queryOk && priorityOk && timeOk
    }
}

fun List<Todo>.filterByStatus(mode: StatusFilter): List<Todo> = filter { todo ->
    when (mode) {
        StatusFilter.ALL -> true
        StatusFilter.ACTIVE -> todo.status != TodoStatus.COMPLETED
        StatusFilter.COMPLETED -> todo.status == TodoStatus.COMPLETED
    }
}

fun List<Todo>.dueOnDay(dayStart: Long): List<Todo> {
    val next = startOfDay(dayStart, 1)
    return filter { val d = it.dueDate; d != null && d >= dayStart && d < next }
}

fun startOfDay(millis: Long, plusDays: Int = 0): Long =
    Calendar.getInstance().apply {
        timeInMillis = millis
        add(Calendar.DAY_OF_YEAR, plusDays)
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis

fun Todo.isOverdue(now: Long = System.currentTimeMillis()): Boolean {
    val due = dueDate ?: return false
    return due < startOfDay(now) && status != TodoStatus.COMPLETED
}