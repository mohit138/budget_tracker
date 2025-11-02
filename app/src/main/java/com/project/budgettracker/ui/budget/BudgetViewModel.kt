package com.project.budgettracker.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.budgettracker.data.entities.Category
import com.project.budgettracker.data.repositories.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class BudgetViewModel(categoryRepository: CategoryRepository): ViewModel() {
    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = emptyList()
    )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}