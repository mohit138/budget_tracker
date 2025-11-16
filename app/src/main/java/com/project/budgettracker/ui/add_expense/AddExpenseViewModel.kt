package com.project.budgettracker.ui.add_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.budgettracker.data.entities.Category
import com.project.budgettracker.data.entities.Expense
import com.project.budgettracker.data.repositories.CategoryRepository
import com.project.budgettracker.data.repositories.ExpensesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.time.LocalDate

class AddExpenseViewModel(
    private val expensesRepository: ExpensesRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    // Private mutable state for user-entered expense details
    private val _expenseDetails = MutableStateFlow(ExpenseDetails())

    // Combined UI state exposed to the UI
    val uiState: StateFlow<AddExpenseUiState> =
        combine(
            categoryRepository.getAllCategories(),
            _expenseDetails
        ) { categories, expenseDetails ->
            AddExpenseUiState(
                categories = categories,
                expenseDetails = expenseDetails,
                isEntryValid = validateInput(expenseDetails)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = AddExpenseUiState()
        )

    fun updateExpenseUiState(expenseDetails: ExpenseDetails) {
        _expenseDetails.value = expenseDetails
    }

    suspend fun saveExpense() {
        if (uiState.value.isEntryValid) {
            expensesRepository.insertExpense(uiState.value.expenseDetails.toExpense())
        }
    }

    private fun validateInput(expenseDetails: ExpenseDetails): Boolean {
        return with(expenseDetails) {
            amount.isNotBlank() && description.isNotBlank() && categoryId != 0
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updateAmountIfEmpty(amount: String) {
        val current = _expenseDetails.value
        if (current.amount.isBlank()) {
            _expenseDetails.value = current.copy(amount = amount)
        }
    }
}

// Single, consolidated UI state data class
data class AddExpenseUiState(
    val categories: List<Category> = emptyList(),
    val expenseDetails: ExpenseDetails = ExpenseDetails(),
    val isEntryValid: Boolean = false
)

data class ExpenseDetails(
    val id: Int = 0,
    val categoryId: Int = 0,
    val amount: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now()
)

fun ExpenseDetails.toExpense(): Expense = Expense(
    id = id,
    categoryId = categoryId,
    amount = amount.toDoubleOrNull() ?: 0.0,
    description = description,
    date = Date.valueOf(date.toString())
)
