package com.example.todoapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.domain.model.Todo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TodoAdapter(
            onToggle = { todo -> viewModel.toggleCompleted(todo) },
            onClick = { todo ->
                findNavController().navigate(
                    R.id.addTodoFragment,
                    bundleOf("todoId" to todo.id)
                )
            },
            onDelete = { todo -> showDeleteDialog(todo) }
        )
        binding.rvTodos.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todos.collect { adapter.submitList(it) }
            }
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.addTodoFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDeleteDialog(todo: Todo) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete task?")
            .setMessage("Do you want to delete this permanently?")
//            .setMessage("\"${todo.title}\" permanently delete ho jayega.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ -> viewModel.delete(todo) }
            .show()
    }
}
