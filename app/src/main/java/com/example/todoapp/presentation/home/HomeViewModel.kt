package com.example.todoapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.domain.model.Priority
import com.example.todoapp.domain.model.TimeFilter
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.model.TodoFilter
import com.example.todoapp.domain.model.applyFilter
import com.example.todoapp.domain.repository.RepoInterface
import com.example.todoapp.presentation.common.TodoActionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: RepoInterface
) : TodoActionsViewModel(repository) {

    private val allTodos: LiveData<List<Todo>> = repository.getTodos()
    private val _filter = MutableLiveData(TodoFilter())
    val filter: LiveData<TodoFilter> = _filter

    var isSearchOpen = false   // rotation par search ki halat yaad rahe

    val todos: LiveData<List<Todo>> = MediatorLiveData<List<Todo>>().apply {
        addSource(allTodos) { list -> value = list.applyFilter(_filter.value!!) }
        addSource(_filter) { f -> value = allTodos.value.orEmpty().applyFilter(f) }
    }

    fun setTime(value: TimeFilter) = updateFilter { it.copy(time = value) }
    fun setPriority(value: Priority?) = updateFilter { it.copy(priority = value) }
    fun setQuery(value: String) = updateFilter { it.copy(query = value) }



    private fun updateFilter(change: (TodoFilter) -> TodoFilter) {
        _filter.value = change(_filter.value!!)
    }
}