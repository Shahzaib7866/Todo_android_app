package com.example.todoapp.presentation.calender

import androidx.core.view.isVisible
import com.example.todoapp.R
import com.example.todoapp.core.shared.BaseFragment
import com.example.todoapp.utils.DueDateUtils
import com.example.todoapp.databinding.CalenderBinding
import com.example.todoapp.presentation.calendar.CalenderViewModel
import com.example.todoapp.utils.createTodoAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class CalenderFragment : BaseFragment<CalenderBinding, CalenderViewModel>(
    CalenderBinding::inflate,
    CalenderViewModel::class.java
) {

    private lateinit var todoAdapter: com.example.todoapp.utils.TodoAdapter // Apne adapter ki exact type yahan rakh len

    override fun onViewReady() {
        setupAdapter()
        setupCalendar()
    }

    override fun observeViewModel() {
        observeTodos()
        observeSelectedDate()
    }

    private fun setupAdapter() {
        // Agar createTodoAdapter aap ka adapter return karta hai
        val adapter = createTodoAdapter(viewModel)
        todoAdapter = adapter as? com.example.todoapp.utils.TodoAdapter ?: return
        binding.rvTodos.adapter = adapter
    }

    private fun setupCalendar() {
        // Safe check taake agar value null ho toh default date utha le
        viewModel.selectedDay.value?.let {
            binding.calendarView.date = it
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            val dayStart = Calendar.getInstance().apply {
                clear()
                set(year, month, day)
            }.timeInMillis
            viewModel.selectDay(dayStart)
        }
    }

    private fun observeTodos() {
        viewModel.todos.observe(viewLifecycleOwner) { list ->
            todoAdapter.submitList(list)
            binding.txtEmpty.isVisible = list.isEmpty()
        }
    }

    private fun observeSelectedDate() {
        viewModel.selectedDay.observe(viewLifecycleOwner) { dateMillis ->
            dateMillis?.let {
                binding.txtSelectedDate.text = getString(R.string.tasks_due_on, DueDateUtils.format(it))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // onScreenResumed ki jagah standard onResume use kiya hai
        binding.rvTodos.adapter?.notifyDataSetChanged()
    }
}