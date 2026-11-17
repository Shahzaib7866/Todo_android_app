package com.example.todoapp.domain.model

data class Todo(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val status: TodoStatus = TodoStatus.PENDING,
    val priority: Priority = Priority.LOW,
    val dueDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    val isCompleted: Boolean get() = status == TodoStatus.COMPLETED
}