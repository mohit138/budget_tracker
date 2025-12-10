package com.project.budgettracker.data.repositories

import com.project.budgettracker.data.dao.ExpenseDao
import com.project.budgettracker.data.entities.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ExpensesRepositoryImpl(
    private val expenseDao: ExpenseDao
) : ExpensesRepository {

    override suspend fun insertExpense(expense: Expense) {
        expenseDao.insert(expense)
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.update(expense)
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.delete(expense)
    }

    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    override fun getExpensesForCategory(categoryId: Int): Flow<List<Expense>> {
        return expenseDao.getExpensesForCategory(categoryId)
    }

    override fun getExpensesBetweenDates(startDate: Date, endDate: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesBetweenDates(startDate, endDate)
    }
}
