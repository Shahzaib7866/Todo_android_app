package com.shahzy.todoapp.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    // Yahan baad mein Repository ya UseCases inject honge
) : ViewModel() {
    // Tasks ka data yahan handle hoga
}