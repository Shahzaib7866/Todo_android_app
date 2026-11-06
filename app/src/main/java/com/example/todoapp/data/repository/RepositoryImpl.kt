package com.example.todoapp.data.repository

import com.example.todoapp.data.database.dao.TodoDao
import com.example.todoapp.data.mapper.toDomain
import com.example.todoapp.data.mapper.toEntity
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.RepoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : RepoInterface {

    override fun getTodos(): Flow<List<Todo>> =
        dao.getAllTodos().map { list -> list.map { it.toDomain() } }

    override suspend fun getTodo(id: Int): Todo? =
        dao.getTodoById(id)?.toDomain()

    override suspend fun saveTodo(todo: Todo) {
        dao.insert(todo.toEntity())
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.delete(todo.toEntity())
    }
}