package com.project.budgettracker.ui.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.budgettracker.data.repositories.CategoryRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditCategoryViewModel (
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    private val categoryId: Int = checkNotNull(savedStateHandle[EditCategoryDestination.categoryIdArg])

    init {
        viewModelScope.launch {
            categoryUiState = categoryRepository.getCategoryById(categoryId)
                .filterNotNull()
                .first()
                .toCategoryUiState()
//            Log.d("EditCategoryViewModel", "CategoryUiState: $categoryUiState")
        }
    }

    suspend fun updateCategory() {
        categoryRepository.updateCategory(categoryUiState.toCategory())
    }

    fun updateCategoryUiState(categoryDetails: CategoryDetails) {
        categoryUiState = CategoryUiState(categoryDetails = categoryDetails)
    }
}
