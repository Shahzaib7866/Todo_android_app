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

    private lateinit var todoAdapter: com.example.todoapp.utils.TodoAdapter

    override fun onViewReady() {
        setupAdapter()
        setupCalendar()
    }

    override fun observeViewModel() {
        observeTodos()
        observeSelectedDate()
    }

    private fun setupAdapter() {
        todoAdapter = createTodoAdapter(viewModel)
        binding.rvTodos.adapter = todoAdapter
    }

    private fun setupCalendar() {
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
        binding.rvTodos.adapter?.notifyDataSetChanged()
    }
}