package com.example.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.database.dao.TodoDao
import com.example.todoapp.core.database.entity.toDomain
import com.example.todoapp.core.database.entity.toEntity
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.RepoInterface
import androidx.lifecycle.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : RepoInterface {

    override fun getTodos(): LiveData<List<Todo>> =
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