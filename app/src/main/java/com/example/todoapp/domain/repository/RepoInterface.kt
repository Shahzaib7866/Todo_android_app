package com.example.todoapp.domain.repository

import com.example.todoapp.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface RepoInterface {

        fun getTodos(): Flow<List<Todo>>
        suspend fun getTodo(id: Int): Todo?
        suspend fun saveTodo(todo: Todo)
        suspend fun deleteTodo(todo: Todo)

}