package com.project.budgettracker

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.project.budgettracker.ui.add_expense.AddExpenseViewModel
import com.project.budgettracker.ui.categories.AddCategoryViewModel
import com.project.budgettracker.ui.categories.CategoriesViewModel
import com.project.budgettracker.ui.categories.EditCategoryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            CategoriesViewModel(inventoryApplication().container.categoryRepository)
        }

        initializer {
            EditCategoryViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.categoryRepository
            )
        }

        initializer {
            AddCategoryViewModel(inventoryApplication().container.categoryRepository)
        }

        initializer {
            AddExpenseViewModel(
                inventoryApplication().container.expensesRepository,
                inventoryApplication().container.categoryRepository
            )
        }
    }
}
fun CreationExtras.inventoryApplication(): BudgetTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as BudgetTrackerApplication)