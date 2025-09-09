package com.example.zobazeexpensetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zobazeexpensetracker.listeners.ExpenseRepository
import com.example.zobazeexpensetracker.room.RoomExpenseRepository
import com.example.zobazeexpensetracker.viewmodels.ExpenseViewModel

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