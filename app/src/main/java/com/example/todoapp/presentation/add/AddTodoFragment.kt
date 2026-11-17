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
        // Header/button ka text aur Status section (sirf Edit mode mein dikhta hai)
        binding.txtHeader.setText(viewModel.headerTitleRes)
        binding.btnSave.setText(viewModel.buttonTextRes)
        binding.layoutStatus.isVisible = viewModel.isEditMode

        setupChips()
        renderForm()   // ViewModel ki halat ke mutabiq chips check karo

        // Save button: title khali ho to error, warna ViewModel ko do
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
        // Edit mode: task DB se aaye to text bharo aur chips refresh karo
        viewModel.todoState.observe(viewLifecycleOwner) { todo ->
            if (todo != null) {
                // Rotation par text dobara overwrite na ho
                if (binding.inputTitle.text.toString().isEmpty()) {
                    binding.inputTitle.setText(todo.title)
                    binding.inputDescription.setText(todo.description)
                }
                renderForm()
            }
        }

        // Save ho jane par wapas jao (extension flow use kiya hai)
        observeFlow(viewModel.saveSuccessEvent) {
            findNavController().popBackStack()
        }
    }

    private fun setupChips() {
        // Priority: chip badle to ViewModel ko batao
        binding.chipGroupPriority.setOnCheckedStateChangeListener { _, ids ->
            viewModel.setPriority(if (ids.firstOrNull() == R.id.chipPrioHigh) Priority.HIGH else Priority.LOW)
        }

        // Status (sirf Edit mode)
        binding.chipGroupStatus.setOnCheckedStateChangeListener { _, ids ->
            viewModel.setStatus(
                when (ids.firstOrNull()) {
                    R.id.chipStInProgress -> TodoStatus.IN_PROGRESS
                    R.id.chipStCompleted -> TodoStatus.COMPLETED
                    else -> TodoStatus.PENDING
                }
            )
        }

        // Today: aaj ki tareekh khud lagti hai
        binding.chipDueToday.setOnClickListener { viewModel.selectToday(); renderForm() }
        // Upcoming: date picker kholo
        binding.chipDueUpcoming.setOnClickListener { showUpcomingDatePicker() }
    }

    // ViewModel ki halat screen par dikhana
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