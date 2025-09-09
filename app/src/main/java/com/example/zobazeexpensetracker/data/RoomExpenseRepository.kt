package com.example.zobazeexpensetracker.data

import java.util.Calendar

class RoomExpenseRepository(private val dao: ExpenseDao) : ExpenseRepository {

    override fun getAllExpensesFlow() = dao.getAllExpensesFlow()

    override suspend fun getAllExpenses() = dao.getAllExpenses()

    override suspend fun insertExpense(expense: Expense): Boolean {
        dao.insertExpense(expense)
        return true
    }

    override suspend fun deleteExpense(id: Int) {
        dao.deleteExpense(id)
    }

    override suspend fun getExpensesForDay(dayMillis: Long): List<Expense> {
        val cal = Calendar.getInstance().apply { timeInMillis = dayMillis }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        val end = start + (24 * 60 * 60 * 1000) - 1
        return dao.getExpensesForDay(start, end)
    }
}