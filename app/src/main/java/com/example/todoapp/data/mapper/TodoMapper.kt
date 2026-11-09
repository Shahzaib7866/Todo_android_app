package com.example.todoapp.data.mapper

import com.example.todoapp.data.database.TodoEntity
import com.example.todoapp.domain.model.Todo

fun TodoEntity.toDomain(): Todo = Todo(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt
)

fun Todo.toEntity(): TodoEntity = TodoEntity(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt
)