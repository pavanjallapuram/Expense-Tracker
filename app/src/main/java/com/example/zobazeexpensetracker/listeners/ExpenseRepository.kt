package com.example.zobazeexpensetracker.listeners

import com.example.zobazeexpensetracker.data.Expense
import com.example.zobazeexpensetracker.room.CategoryTotal
import com.example.zobazeexpensetracker.room.DailyTotal
import com.example.zobazeexpensetracker.room.ExpenseDao

import kotlinx.coroutines.flow.Flow


interface ExpenseRepository {
    fun getAllExpensesFlow(): Flow<List<Expense>>
    suspend fun getAllExpenses(): List<Expense>
    suspend fun insertExpense(expense: Expense): Boolean
    suspend fun deleteExpense(id: Int)

    suspend fun getExpensesForDay(dayMillis: Long): List<Expense>
}