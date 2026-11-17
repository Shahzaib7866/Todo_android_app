package com.example.todoapp.presentation.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.domain.model.StatusFilter
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.model.filterByStatus
import com.example.todoapp.domain.repository.RepoInterface
import com.example.todoapp.presentation.common.TodoActionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    repository: RepoInterface
) : TodoActionsViewModel(repository) {

    private val allTodos: LiveData<List<Todo>> = repository.getTodos()
    private val _filter = MutableLiveData(StatusFilter.ALL)
    val filter: LiveData<StatusFilter> = _filter

    val todos: LiveData<List<Todo>> = MediatorLiveData<List<Todo>>().apply {
        addSource(allTodos) { list -> value = list.filterByStatus(_filter.value!!) }
        addSource(_filter) { f -> value = allTodos.value.orEmpty().filterByStatus(f) }
    }

    fun setFilter(value: StatusFilter) { _filter.value = value }
}