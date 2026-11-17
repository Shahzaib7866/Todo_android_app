package com.example.todoapp.presentation.home

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.core.shared.BaseFragment
import com.example.todoapp.databinding.HomeBinding
import com.example.todoapp.domain.model.Priority
import com.example.todoapp.domain.model.TimeFilter
import com.example.todoapp.utils.createTodoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
  class HomeFragment : BaseFragment<HomeBinding, HomeViewModel>(
        HomeBinding::inflate,
        HomeViewModel::class.java
    ) {

    // Search khuli ho to Back pehle search band kare
    private val searchBackCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() = closeSearch()
    }

    override fun onViewReady() {
        setupPriorityChips()
        setupFilterButton()
        setupSearch()
        binding.fabAdd.setOnClickListener { findNavController().navigate(R.id.addTodoFragment) }
    }

    override fun observeViewModel() {
        setupList()
    }

    private fun setupList() {
        val adapter = createTodoAdapter(viewModel)
        binding.rvTodos.adapter = adapter
        viewModel.todos.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtEmpty.isVisible = list.isEmpty()
        }
    }

    private fun setupSearch() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            searchBackCallback
        )
        renderSearchMode()
        binding.btnSearch.setOnClickListener {
            if (viewModel.isSearchOpen) closeSearch() else openSearch()
        }
        binding.etSearch.doAfterTextChanged { viewModel.setQuery(it?.toString().orEmpty()) }
    }

    private fun openSearch() {
        viewModel.isSearchOpen = true
        renderSearchMode()
        binding.etSearch.requestFocus()
        binding.etSearch.post {
            imm().showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun closeSearch() {
        binding.etSearch.setText("")          // query bhi saaf ho jati hai
        viewModel.isSearchOpen = false
        renderSearchMode()
        imm().hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun renderSearchMode() {
        val open = viewModel.isSearchOpen
        binding.txtHeader.visibility = if (open) View.INVISIBLE else View.VISIBLE
        binding.btnFilter.isVisible = !open
        binding.searchLayout.isVisible = open
        searchBackCallback.isEnabled = open
    }

    private fun imm() = requireContext().getSystemService(InputMethodManager::class.java)

    // ---------- Filter button (Time) ----------
    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            val current = viewModel.filter.value?.time ?: TimeFilter.ALL
            PopupMenu(requireContext(), binding.btnFilter).apply {
                menuInflater.inflate(R.menu.time_filter, menu)
                menu.findItem(itemIdFor(current)).isChecked = true
                setOnMenuItemClickListener { item ->
                    viewModel.setTime(timeFor(item.itemId))
                    true
                }
                show()
            }
        }
    }

    private fun itemIdFor(time: TimeFilter) = when (time) {
        TimeFilter.ALL -> R.id.time_all
        TimeFilter.TODAY -> R.id.time_today
        TimeFilter.UPCOMING -> R.id.time_upcoming
        TimeFilter.OVERDUE -> R.id.time_overdue
    }

    private fun timeFor(itemId: Int) = when (itemId) {
        R.id.time_today -> TimeFilter.TODAY
        R.id.time_upcoming -> TimeFilter.UPCOMING
        R.id.time_overdue -> TimeFilter.OVERDUE
        else -> TimeFilter.ALL
    }

    // ---------- Priority chips ----------
    private fun setupPriorityChips() {
        // Screen khulte waqt ViewModel mein jo priority hai wahi chip checked dikhao
        binding.chipGroupPriority.check(
            when (viewModel.filter.value?.priority) {
                Priority.HIGH -> R.id.chipHigh
                Priority.LOW -> R.id.chipLow
                null -> R.id.chipAll
            }
        )
        // User chip badle to ViewModel ko naya filter batao (null = sab priorities)
        binding.chipGroupPriority.setOnCheckedStateChangeListener { _, ids ->
            viewModel.setPriority(
                when (ids.firstOrNull()) {
                    R.id.chipHigh -> Priority.HIGH
                    R.id.chipLow -> Priority.LOW
                    else -> null
                }
            )
        }
    }

    // BaseFragment ka hook: screen wapas aaye to rows dobara bind karo (Overdue label tazah ho)
     fun onScreenResumed() {
        binding.rvTodos.adapter?.notifyDataSetChanged()
    }
}
