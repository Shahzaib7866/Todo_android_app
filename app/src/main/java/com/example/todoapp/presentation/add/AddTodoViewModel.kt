package com.example.todoapp.presentation.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.R
import com.example.todoapp.domain.model.Priority
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.model.TodoStatus
import com.example.todoapp.domain.model.startOfDay
import com.example.todoapp.domain.repository.RepoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Add screen ke 3 due chips
enum class DueOption { TODAY, UPCOMING, OVERDUE }

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val repository: RepoInterface,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Navigation se aaya todoId; na mile to -1 (matlab naya task)
    private val todoId: Int = savedStateHandle.get<Int>("todoId") ?: -1
    val isEditMode: Boolean = todoId != -1

    // Header aur button ka text, mode ke hisaab se
    val headerTitleRes: Int = if (isEditMode) R.string.edit_task else R.string.add_task
    val buttonTextRes: Int = if (isEditMode) R.string.update else R.string.save

    // ---- Form ki halat ViewModel mein rakhi hai taake rotation par na ghaibe ----
    var priority: Priority = Priority.LOW          // default Low
        private set
    var status: TodoStatus = TodoStatus.PENDING    // naya task hamesha Pending
        private set
    var dueOption: DueOption = DueOption.TODAY     // default Today
        private set
    var dueDate: Long = startOfDay(System.currentTimeMillis())   // din ki shuruaat (millis)
        private set

    // Edit mode mein DB se aaya hua task
    private val _todoState = MutableLiveData<Todo?>()
    val todoState: LiveData<Todo?> = _todoState
    private var existingTodo: Todo? = null

    // Save hone ke baad "wapas jao" event (Channel = event ek hi baar milta hai)
    private val _saveSuccessEvent = Channel<Unit>()
    val saveSuccessEvent = _saveSuccessEvent.receiveAsFlow()

    init {
        if (isEditMode) {
            viewModelScope.launch {
                val todo = repository.getTodo(todoId)
                existingTodo = todo
                if (todo != null) {
                    // Task ki values form ki halat mein bharo
                    priority = todo.priority
                    status = todo.status
                    // Purane tasks jin ki due date nahi, unhein Today maan lo
                    val due = todo.dueDate ?: startOfDay(System.currentTimeMillis())
                    dueDate = due
                    dueOption = optionFor(due)
                }
                _todoState.value = todo
            }
        }
    }

    // Tareekh dekh kar batata hai ke wo Today hai, Upcoming ya Overdue
    private fun optionFor(due: Long): DueOption {
        val now = System.currentTimeMillis()
        return when {
            due < startOfDay(now) -> DueOption.OVERDUE
            due >= startOfDay(now, 1) -> DueOption.UPCOMING
            else -> DueOption.TODAY
        }
    }

    fun setPriority(value: Priority) { priority = value }
    fun setStatus(value: TodoStatus) { status = value }

    // Today chip: aaj ki tareekh khud lag jati hai
    fun selectToday() {
        dueOption = DueOption.TODAY
        dueDate = startOfDay(System.currentTimeMillis())
    }

    // Upcoming / Overdue: picker se chuni hui tareekh
    fun setDate(option: DueOption, dayStart: Long) {
        dueOption = option
        dueDate = dayStart
    }

    // Save ya Update: Edit mode mein purane task ki copy, warna naya Todo
    fun saveTodo(title: String, description: String) {
        val t = title.trim()
        val d = description.trim()
        viewModelScope.launch {
            val todo = existingTodo?.copy(
                title = t, description = d,
                status = status, priority = priority, dueDate = dueDate
            ) ?: Todo(
                title = t, description = d,
                status = status, priority = priority, dueDate = dueDate
            )
            repository.saveTodo(todo)
            _saveSuccessEvent.send(Unit)
        }
    }
}
