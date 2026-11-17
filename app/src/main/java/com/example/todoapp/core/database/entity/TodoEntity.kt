package com.example.todoapp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.model.TodoStatus
import com.example.todoapp.domain.model.Priority

//ye entity annotation ka mutlab hai k ye class humari db ka aik table bn jahy gii
@Entity(tableName = "Todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val status: Int = TodoStatus.PENDING.value,
    val priority: Int = Priority.LOW.value,
    val dueDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

fun TodoEntity.toDomain() = Todo(
    id = id,
    title = title,
    description = description,
    status = TodoStatus.fromValue(status),
    priority = Priority.fromValue(priority),
    dueDate = dueDate, createdAt = createdAt
)

fun Todo.toEntity() = TodoEntity(
    id = id, title = title, description = description,
    status = status.value, priority = priority.value,
    dueDate = dueDate, createdAt = createdAt
)
