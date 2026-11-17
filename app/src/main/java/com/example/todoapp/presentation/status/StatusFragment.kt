package com.example.todoapp.presentation.status

import android.os.Bundle
import androidx.core.view.isVisible
import com.example.todoapp.R
import com.example.todoapp.core.shared.BaseFragment
import com.example.todoapp.databinding.StatusBinding
import com.example.todoapp.domain.model.StatusFilter
import com.example.todoapp.utils.TodoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatusFragment : BaseFragment<StatusBinding, StatusViewModel>(
    StatusBinding::inflate,
    StatusViewModel::class.java
) {

    private lateinit var todoAdapter: TodoAdapter

    override fun onViewReady() {
        // Yahan ViewModel ke asli functions (toggleCompleted aur delete) pass kiye hain
        todoAdapter = TodoAdapter(
            onToggle = { todo -> viewModel.toggleCompleted(todo) },
            onClick = { /* Agar item click par kuch kholna ho */ },
            onDelete = { todo -> viewModel.delete(todo) }
        )
        binding.rvTodos.adapter = todoAdapter

        // Chip group filter setup
        binding.chipGroupStatus.check(
            when (viewModel.filter.value) {
                StatusFilter.ACTIVE -> R.id.chipActive
                StatusFilter.COMPLETED -> R.id.chipCompleted
                else -> R.id.chipStatusAll
            }
        )

        binding.chipGroupStatus.setOnCheckedStateChangeListener { _, ids ->
            viewModel.setFilter(
                when (ids.firstOrNull()) {
                    R.id.chipActive -> StatusFilter.ACTIVE
                    R.id.chipCompleted -> StatusFilter.COMPLETED
                    else -> StatusFilter.ALL
                }
            )
        }
    }

    override fun observeViewModel() {
        viewModel.todos.observe(viewLifecycleOwner) { list ->
            todoAdapter.submitList(list)
            binding.txtEmpty.isVisible = list.isEmpty()
        }
    }

    override fun onResume() {
        super.onResume()
        // Screen wapas aane par list refresh karna taake overdue label update rahe
        binding.rvTodos.adapter?.notifyDataSetChanged()
    }
}