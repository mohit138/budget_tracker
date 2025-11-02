package com.project.budgettracker.data

import android.content.Context
import com.project.budgettracker.data.repositories.CategoryRepository
import com.project.budgettracker.data.repositories.CategoryRepositoryImpl
import com.project.budgettracker.data.repositories.ExpensesRepository
import com.project.budgettracker.data.repositories.ExpensesRepositoryImpl
import kotlin.getValue

interface AppContainer {
    val categoryRepository: CategoryRepository
    val expensesRepository: ExpensesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val categoryRepository: CategoryRepository by lazy {
        CategoryRepositoryImpl(BudgetTrackerDatabase.getDatabase(context).categoryDao())
    }

    override val expensesRepository: ExpensesRepository by lazy {
        ExpensesRepositoryImpl(BudgetTrackerDatabase.getDatabase(context).expenseDao())
    }
}