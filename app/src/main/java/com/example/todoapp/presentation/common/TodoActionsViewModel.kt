package com.example.todoapp.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.model.TodoStatus
import com.example.todoapp.domain.repository.RepoInterface
import kotlinx.coroutines.launch

abstract class TodoActionsViewModel(
    protected val repository: RepoInterface
) : ViewModel() {

    fun toggleCompleted(todo: Todo) = viewModelScope.launch {
        val newStatus = if (todo.isCompleted) TodoStatus.PENDING else TodoStatus.COMPLETED
        repository.saveTodo(todo.copy(status = newStatus))
    }

    fun delete(todo: Todo) = viewModelScope.launch { repository.deleteTodo(todo) }
}