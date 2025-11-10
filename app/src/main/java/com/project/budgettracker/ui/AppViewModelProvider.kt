
package com.project.budgettracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.project.budgettracker.BudgetTrackerApplication
import com.project.budgettracker.ui.add_expense.AddExpenseViewModel
import com.project.budgettracker.ui.budget.BudgetViewModel
import com.project.budgettracker.ui.categories.EditCategoryViewModel
import com.project.budgettracker.ui.categories.AddCategoryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AddExpenseViewModel
        initializer {
            AddExpenseViewModel(
                budgetTrackerApplication().container.expensesRepository,
                budgetTrackerApplication().container.categoryRepository
            )
        }

        // Initializer for AddCategoryViewModel
        initializer {
            AddCategoryViewModel(
                budgetTrackerApplication().container.categoryRepository,
            )
        }

        // Initializer for EditCategoryViewModel
        initializer {
            EditCategoryViewModel(
                this.createSavedStateHandle(),
                budgetTrackerApplication().container.categoryRepository
            )
        }

        // Initializer for BudgetViewModel
        initializer {
            BudgetViewModel(
                budgetTrackerApplication().container.expensesRepository,
                budgetTrackerApplication().container.categoryRepository
            )
        }
    }
}

fun CreationExtras.budgetTrackerApplication(): BudgetTrackerApplication = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BudgetTrackerApplication)
