package com.example.zobazeexpensetracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zobazeexpensetracker.data.Expense
import com.example.zobazeexpensetracker.data.ExpenseUiState
import com.example.zobazeexpensetracker.listeners.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ExpenseViewModel(private val repo: ExpenseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    init {
        loadExpenses()
    }


    fun setFilterDate(millis: Long?) {
        viewModelScope.launch {
            val filtered = if (millis != null) {
                repo.getExpensesForDay(millis)
            } else {
                repo.getAllExpenses()
            }
            _uiState.update {
                it.copy(
                    filterDateMillis = millis,
                    expenses = filtered,
                    totalToday = calculateTotalToday(filtered)
                )
            }
        }
    }


    private fun calculateTotalToday(expenses: List<Expense>): Double {
        val todayMillis = System.currentTimeMillis()
        val cal = Calendar.getInstance().apply { timeInMillis = todayMillis }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        val end = start + (24 * 60 * 60 * 1000) - 1

        return expenses.filter { it.dateMillis in start..end }
            .sumOf { it.amount }
    }

    /** ✅ Load all expenses & compute totals */
    private fun loadExpenses() {
        viewModelScope.launch {
            repo.getAllExpensesFlow().collect { expenses ->
                val todayMillis = System.currentTimeMillis()
                val cal = Calendar.getInstance()
                cal.timeInMillis = todayMillis

                val todayTotal = expenses.filter {
                    isSameDay(it.dateMillis, todayMillis)
                }.sumOf { it.amount }

                _uiState.update {
                    it.copy(
                        expenses = expenses,
                        totalToday = todayTotal
                    )
                }
            }
        }
    }

    /** ✅ Add new expense */
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repo.insertExpense(expense)
            _uiState.update { it.copy(message = "Expense added!") }
        }
    }

    /** ✅ Delete expense */
    fun deleteExpense(id: Int) {
        viewModelScope.launch {
            repo.deleteExpense(id)
            _uiState.update { it.copy(message = "Expense deleted!") }
        }
    }

    /** ✅ Switch theme */
    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    /** ✅ Group toggle */
    fun toggleGroupByCategory() {
        _uiState.update { it.copy(groupByCategory = !it.groupByCategory) }
    }

    /** ✅ Report for last 7 days */
    suspend fun last7DaysReport(): Pair<List<Pair<String, Double>>, Map<String, Double>> {
        val all = repo.getAllExpenses()
        val cal = Calendar.getInstance()

        val dayTotals = mutableListOf<Pair<String, Double>>()
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        // ✅ Daily totals
        for (i in 0 until 7) {
            cal.timeInMillis = System.currentTimeMillis()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val dayStart = startOfDay(cal.timeInMillis)
            val dayEnd = endOfDay(cal.timeInMillis)

            val dailySum = all.filter { it.dateMillis in dayStart..dayEnd }
                .sumOf { it.amount }

            dayTotals.add(dateFormat.format(cal.time) to dailySum)
        }

        // ✅ Category totals
        val categoryTotals = all.filter {
            it.dateMillis >= System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
        }.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        return dayTotals.reversed() to categoryTotals
    }

    /** ✅ Helpers */
    private fun isSameDay(millis1: Long, millis2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = millis1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = millis2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun startOfDay(millis: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun endOfDay(millis: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
}