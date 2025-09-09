package com.example.zobazeexpensetracker.data

data class ExpenseUiState(
    val currentDateMillis: Long = System.currentTimeMillis(),
    val expenses: List<Expense> = emptyList(),
    val totalToday: Double = 0.0,
    val isDarkTheme: Boolean = false,
    val groupByCategory: Boolean = false,
    val filterDateMillis: Long? = null,
    val message: String? = null // transient toast-like message
)
