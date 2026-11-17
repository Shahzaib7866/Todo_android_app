package com.example.todoapp.domain.model

// HIGH ki value 2 hi rakhi taake purana saved data kharab na ho
enum class Priority(val value: Int) {
    LOW(0), HIGH(2);

    companion object {
        // Database ka Int wapas enum banata hai; na mile to LOW (purana Medium bhi LOW ban jayega)
        fun fromValue(v: Int) = values().firstOrNull { it.value == v } ?: LOW
    }
}

enum class TodoStatus(val value: Int) {
    PENDING(0), IN_PROGRESS(1), COMPLETED(2);

    companion object {
        fun fromValue(v: Int) = values().firstOrNull { it.value == v } ?: PENDING
    }
}