package com.example.todoapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.RepoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RepoInterface
) : ViewModel() {

    val todos: StateFlow<List<Todo>> = repository.getTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleCompleted(todo: Todo) = viewModelScope.launch {
        repository.saveTodo(todo.copy(isCompleted = !todo.isCompleted))
    }

    fun delete(todo: Todo) = viewModelScope.launch { repository.deleteTodo(todo) }
}