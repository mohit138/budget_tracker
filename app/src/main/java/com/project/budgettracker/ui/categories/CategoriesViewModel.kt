package com.project.budgettracker.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.budgettracker.data.entities.Category
import com.project.budgettracker.data.repositories.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.text.toDoubleOrNull

class CategoriesViewModel(categoryRepository: CategoryRepository): ViewModel() {
    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = emptyList()
    )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CategoryUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
)

data class CategoryDetails(
    val id: Int = 0,
    val name: String = "",
    val budget: String = "",
)


/**
 * Extension function to convert [CategoryUiState] to [Category]. If the value of [CategoryDetails.id] is
 * not specified, it creates a new [Category] with [CategoryDetails]
 * */
fun CategoryUiState.toCategory(): Category = categoryDetails.toCategory()

fun CategoryDetails.toCategory(): Category = Category(
    id = id,
    name = name,
    budget = budget.toDoubleOrNull() ?: 0.0
)


/**
 * Functions to convert [Category] to [CategoryUiState] or vice versa
 */
fun Category.toCategoryUiState(): CategoryUiState = CategoryUiState(
    categoryDetails = this.toCategoryDetails()
)
fun Category.toCategoryDetails(): CategoryDetails = CategoryDetails(
    id = id,
    name = name,
    budget = budget.toString()
)