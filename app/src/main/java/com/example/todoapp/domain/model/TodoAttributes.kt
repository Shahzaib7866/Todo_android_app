package com.example.todoapp.domain.model

enum class Priority(val value: Int) {
    LOW(0), HIGH(2);

    companion object {
        fun fromValue(v: Int) = values().firstOrNull { it.value == v } ?: LOW
    }
}

enum class TodoStatus(val value: Int) {
    PENDING(0), IN_PROGRESS(1), COMPLETED(2);

    companion object {
        fun fromValue(v: Int) = values().firstOrNull { it.value == v } ?: PENDING
    }
}