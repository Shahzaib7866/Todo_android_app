package com.example.todoapp.presentation.add

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.core.shared.BaseFragment
import com.example.todoapp.databinding.AddTodosBinding
import com.example.todoapp.domain.model.Priority
import com.example.todoapp.domain.model.TodoStatus
import com.example.todoapp.domain.model.startOfDay
import com.example.todoapp.utils.DueDateUtils
import com.example.todoapp.utils.observeFlow
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTodoFragment : BaseFragment<AddTodosBinding, AddTodoViewModel>(
    AddTodosBinding::inflate,
    AddTodoViewModel::class.java
) {

    override fun onViewReady() {
        binding.txtHeader.setText(viewModel.headerTitleRes)
        binding.btnSave.setText(viewModel.buttonTextRes)
        binding.layoutStatus.isVisible = viewModel.isEditMode

        setupChips()
        renderForm()


        binding.btnSave.setOnClickListener {
            val title = binding.inputTitle.text.toString()
            if (title.isBlank()) {
                binding.todoTitle.error = getString(R.string.title_required)
                return@setOnClickListener
            }
            binding.todoTitle.error = null
            viewModel.saveTodo(title, binding.inputDescription.text.toString())
        }
    }

    override fun observeViewModel() {
        viewModel.todoState.observe(viewLifecycleOwner) { todo ->
            if (todo != null) {
                if (binding.inputTitle.text.toString().isEmpty()) {
                    binding.inputTitle.setText(todo.title)
                    binding.inputDescription.setText(todo.description)
                }
                renderForm()
            }
        }

        observeFlow(viewModel.saveSuccessEvent) {
            findNavController().popBackStack()
        }
    }

    private fun setupChips() {
        binding.chipGroupPriority.setOnCheckedStateChangeListener { _, ids ->
            viewModel.setPriority(if (ids.firstOrNull() == R.id.chipPrioHigh) Priority.HIGH else Priority.LOW)
        }

        binding.chipGroupStatus.setOnCheckedStateChangeListener { _, ids ->
            viewModel.setStatus(
                when (ids.firstOrNull()) {
                    R.id.chipStInProgress -> TodoStatus.IN_PROGRESS
                    R.id.chipStCompleted -> TodoStatus.COMPLETED
                    else -> TodoStatus.PENDING
                }
            )
        }

        binding.chipDueToday.setOnClickListener { viewModel.selectToday(); renderForm() }
        binding.chipDueUpcoming.setOnClickListener { showUpcomingDatePicker() }
    }

    private fun renderForm() {
        binding.chipGroupPriority.check(
            if (viewModel.priority == Priority.HIGH) R.id.chipPrioHigh else R.id.chipPrioLow
        )

        when (viewModel.dueOption) {
            DueOption.TODAY -> binding.chipGroupDue.check(R.id.chipDueToday)
            DueOption.UPCOMING -> binding.chipGroupDue.check(R.id.chipDueUpcoming)
            DueOption.OVERDUE -> binding.chipGroupDue.clearCheck()
        }

        binding.chipGroupStatus.check(
            when (viewModel.status) {
                TodoStatus.IN_PROGRESS -> R.id.chipStInProgress
                TodoStatus.COMPLETED -> R.id.chipStCompleted
                else -> R.id.chipStPending
            }
        )

        binding.txtDueDate.text = getString(R.string.tasks_due_on, DueDateUtils.format(viewModel.dueDate))
    }

    private fun showUpcomingDatePicker() {
        val start = DueDateUtils.localToPicker(startOfDay(System.currentTimeMillis(), 1))
        val constraints = CalendarConstraints.Builder()
            .setStart(start)
            .setValidator(DateValidatorPointForward.from(start))
            .build()

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.pick_upcoming_date)
            .setCalendarConstraints(constraints)
            .apply {
                if (viewModel.dueOption == DueOption.UPCOMING) {
                    setSelection(DueDateUtils.localToPicker(viewModel.dueDate))
                }
            }
            .build()

        picker.addOnPositiveButtonClickListener { utc ->
            viewModel.setDate(DueOption.UPCOMING, DueDateUtils.pickerToLocalStart(utc))
        }

        picker.addOnDismissListener { if (view != null) renderForm() }
        picker.show(childFragmentManager, "dueDatePicker")
    }
}