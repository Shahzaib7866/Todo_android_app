package com.example.todoapp.presentation.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.model.dueOnDay
import com.example.todoapp.domain.model.startOfDay
import com.example.todoapp.domain.repository.RepoInterface
import com.example.todoapp.presentation.common.TodoActionsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalenderViewModel @Inject constructor(
    repository: RepoInterface
) : TodoActionsViewModel(repository) {

    private val allTodos: LiveData<List<Todo>> = repository.getTodos()
    private val _selectedDay = MutableLiveData(startOfDay(System.currentTimeMillis()))
    val selectedDay: LiveData<Long> = _selectedDay

    val todos: LiveData<List<Todo>> = MediatorLiveData<List<Todo>>().apply {
        addSource(allTodos) { list -> value = list.dueOnDay(_selectedDay.value!!) }
        addSource(_selectedDay) { day -> value = allTodos.value.orEmpty().dueOnDay(day) }
    }

    fun selectDay(dayStart: Long) { _selectedDay.value = dayStart }
}