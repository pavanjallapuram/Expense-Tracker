package com.example.zobazeexpensetracker.data

import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpensesFlow(): Flow<List<Expense>>
    suspend fun getAllExpenses(): List<Expense>
    suspend fun insertExpense(expense: Expense): Boolean
    suspend fun deleteExpense(id: Int)

    suspend fun getExpensesForDay(dayMillis: Long): List<Expense>
}