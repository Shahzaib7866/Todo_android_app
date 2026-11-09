package com.example.todoapp.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.RepoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val repository: RepoInterface
) : ViewModel() {

    fun loadTodo(id: Int, onLoaded: (Todo) -> Unit) {
        viewModelScope.launch {
            repository.getTodo(id)?.let(onLoaded)
        }
    }

    fun save(existing: Todo?, title: String, description: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            val todo = existing?.copy(title = title.trim(), description = description.trim())
                ?: Todo(title = title.trim(), description = description.trim())
            repository.saveTodo(todo)
            onSaved()
        }
    }
}

