package com.example.todoapp.core.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// VB = us fragment ki binding class (maslan HomeBinding), VM = us ka ViewModel
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    // Binding banane ka tareeqa; subclass sirf apni Binding::inflate dega
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    // ViewModel ki class (maslan HomeViewModel::class.java)
    private val viewModelClass: Class<VM>
) : Fragment() {

    private var _binding: VB? = null
    // Subclass mein seedha "binding" likhna hai (onDestroyView ke baad use na karna)
    protected val binding: VB get() = _binding!!

    // Hilt ke saath bhi kaam karta hai (defaultViewModelProviderFactory use hoti hai)
    protected val viewModel: VM by lazy { ViewModelProvider(this)[viewModelClass] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady()
        observeViewModel()
    }

    // Views setup (click listeners, adapters, etc.)
    protected abstract fun onViewReady()

    // ViewModel ke LiveData/Flow observe karna
    protected abstract fun observeViewModel()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}