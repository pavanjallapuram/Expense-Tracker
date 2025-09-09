package com.example.zobazeexpensetracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zobazeexpensetracker.data.RoomExpenseRepository

class ExpenseViewModelFactory(
    private val repo: RoomExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}