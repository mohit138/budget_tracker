package com.project.budgettracker.ui.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.project.budgettracker.data.repositories.CategoryRepository

class AddCategoryViewModel (
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    fun updateCategoryUiState(categoryDetails: CategoryDetails) {
        categoryUiState = CategoryUiState(categoryDetails = categoryDetails)
    }

    suspend fun insertCategory() {
        categoryRepository.insertCategory(categoryUiState.toCategory())
    }
}