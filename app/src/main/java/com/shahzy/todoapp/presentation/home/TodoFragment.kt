package com.shahzy.todoapp.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.shahzy.todoapp.databinding.FragmentTodoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class TodoFragment : Fragment() {

    // 1. ViewBinding variables
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    // 2. ViewModel initialization (Hilt ke sath automatic)
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 3. Layout ko ViewBinding ke zariye inflate karna
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Yahan aap apne UI elements ya RecyclerView ko initialize karenge
        // Misal ke tor par: binding.txtHomeTitle.text = "Welcome Shahzaib!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 4. Memory leak se bachne ke liye binding ko null karna
        _binding = null
    }
}
