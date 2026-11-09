package com.example.todoapp.presentation.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.databinding.FragmentAddTodosBinding
import com.example.todoapp.domain.model.Todo
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AddTodoFragment : Fragment() {

    private var _binding: FragmentAddTodosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddTodoViewModel by viewModels()
    private var existingTodo: Todo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTodosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Edit mode
        val todoId = arguments?.getInt("todoId", -1) ?: -1
        if (todoId != -1) {
            binding.txtHeader.text = "Edit Task"
            binding.btnSave.text = "Update"

            viewModel.loadTodo(todoId) { todo ->
                existingTodo = todo
                if (savedInstanceState == null) {
                    binding.inputTitle.setText(todo.title)
                    binding.inputDescription.setText(todo.description)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.inputTitle.text.toString()
            val description = binding.inputDescription.text.toString()

            if (title.isBlank()) {
                binding.todoTitle.error = "Title required"
                return@setOnClickListener
            }
            binding.todoTitle.error = null

            viewModel.save(existingTodo, title, description) {
                findNavController().popBackStack()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}