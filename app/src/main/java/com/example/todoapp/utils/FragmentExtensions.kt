package com.example.todoapp.utils

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.domain.model.Todo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


import android.graphics.Color
import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.todoapp.domain.model.TodoStatus
import com.example.todoapp.presentation.common.TodoActionsViewModel

fun Fragment.createTodoAdapter(actions: TodoActionsViewModel): TodoAdapter = TodoAdapter(
    onToggle = { actions.toggleCompleted(it) },
    onClick = { todo ->
        findNavController().navigate(R.id.addTodoFragment, bundleOf("todoId" to todo.id))
    },
    onDelete = { todo -> confirmDelete(todo) { actions.delete(todo) } }
)

private fun Fragment.confirmDelete(todo: Todo, onConfirm: () -> Unit) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle("Delete task?")
        .setMessage("\"${todo.title}\" permanently delete ho jayega.")
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Delete") { _, _ -> onConfirm() }
        .show()
}

fun TextView.showStatus(status: TodoStatus) {
    val (textRes, bg, fg) = when (status) {
        TodoStatus.PENDING -> Triple(R.string.status_pending, "#E3F2FD", "#1565C0")
        TodoStatus.IN_PROGRESS -> Triple(R.string.status_in_progress, "#FFF3E0", "#E65100")
        TodoStatus.COMPLETED -> { isVisible = false; return }
    }
    setText(textRes)
    setTextColor(Color.parseColor(fg))
    backgroundTintList = ColorStateList.valueOf(Color.parseColor(bg))
    isVisible = true
}

fun <T> Fragment.observeFlow(flow: Flow<T>, action: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                action(it)
            }
        }
    }
}
