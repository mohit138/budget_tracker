package com.project.budgettracker.data.repositories

import com.project.budgettracker.data.entities.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ExpensesRepository {
    // Expense methods
    suspend fun insertExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesForCategory(categoryId: Int): Flow<List<Expense>>
    fun getExpensesBetweenDates(startDate: Date, endDate: Date): Flow<List<Expense>>
}
