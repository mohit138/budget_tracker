package com.project.budgettracker.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.budgettracker.data.entities.Category
import com.project.budgettracker.data.entities.Expense
import com.project.budgettracker.data.repositories.CategoryRepository
import com.project.budgettracker.data.repositories.ExpensesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.util.Calendar

data class HistoryScreenUiState(
    val expenses: List<ExpenseWithCategory> = emptyList(),
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
)

data class ExpenseWithCategory(
    val expense: Expense,
    val category: Category?
)

class HistoryViewModel(
    private val expensesRepository: ExpensesRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(Calendar.getInstance())

    // This flow transforms the selected date into a stream of expenses for that date's month.
    @OptIn(ExperimentalCoroutinesApi::class)
    private val expensesFlow = _selectedDate.flatMapLatest { selectedDate ->
        val calendar = selectedDate.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startDate = Date(calendar.timeInMillis)

        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.DATE, -1)
        val endDate = Date(calendar.timeInMillis)

        expensesRepository.getExpensesBetweenDates(startDate, endDate)
    }

    val uiState: StateFlow<HistoryScreenUiState> =
        combine(
            expensesFlow,
            categoryRepository.getAllCategories(),
            _selectedDate
        ) { expenses, categories, date ->
            val expensesWithCategory = expenses.map { expense ->
                ExpenseWithCategory(
                    expense = expense,
                    category = categories.find { it.id == expense.categoryId }
                )
            }
            HistoryScreenUiState(
                expenses = expensesWithCategory,
                selectedYear = date.get(Calendar.YEAR),
                selectedMonth = date.get(Calendar.MONTH)
            )
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryScreenUiState()
        )

    fun onDateChange(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        _selectedDate.value = calendar
    }
}