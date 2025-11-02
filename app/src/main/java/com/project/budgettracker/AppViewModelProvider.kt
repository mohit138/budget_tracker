package com.project.budgettracker

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.project.budgettracker.ui.categories.CategoriesViewModel

class AppViewModelProvider {
    val factory = viewModelFactory {
        initializer {
            CategoriesViewModel(inventoryApplication().container.categoryRepository)
        }
    }
}
fun CreationExtras.inventoryApplication(): BudgetTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as BudgetTrackerApplication)