package com.project.budgettracker.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.budgettracker.data.entities.Category
import com.project.budgettracker.data.entities.Expense
import com.project.budgettracker.data.repositories.CategoryRepository
import com.project.budgettracker.data.repositories.ExpensesRepository
import com.project.budgettracker.ui.components.SummaryRowData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.util.Calendar

/**
 * UI state for the shared budget screens (Home, History, Summary).
 */
data class BudgetUiState(
    // Raw data for History
    val expensesWithCategory: List<ExpenseWithCategory> = emptyList(),

    // Pre-computed data for Summary/Home
    val summaryData: List<SummaryRowData> = emptyList(),
    val netBudgetData: SummaryRowData = SummaryRowData("Net Budget", 0.0, 0.0),

    // Common data
    val allCategories: List<Category> = emptyList(),
    val totalExpenses: Double = 0.0,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
)

data class ExpenseWithCategory(
    val expense: Expense,
    val category: Category?
)

class BudgetViewModel(
    private val expensesRepository: ExpensesRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(Calendar.getInstance())

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

    val uiState: StateFlow<BudgetUiState> =
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
            val totalExpenses = expensesWithCategory.sumOf { it.expense.amount }

            // --- Logic moved from SummaryScreen to ViewModel ---
            val summaryData = expensesWithCategory
                .groupBy { it.category }
                .map { (category, expenseList) ->
                    SummaryRowData(
                        category = category?.name ?: "Uncategorized",
                        amountSpent = expenseList.sumOf { exp -> exp.expense.amount },
                        budget = category?.budget ?: 0.0
                    )
                }

            val totalBudget = categories.sumOf { it.budget }
            val netBudgetData = SummaryRowData(
                category = "Net Budget",
                amountSpent = totalExpenses,
                budget = totalBudget
            )
            // --- End of moved logic ---

            BudgetUiState(
                expensesWithCategory = expensesWithCategory,
                summaryData = summaryData,
                netBudgetData = netBudgetData,
                allCategories = categories,
                totalExpenses = totalExpenses,
                selectedYear = date.get(Calendar.YEAR),
                selectedMonth = date.get(Calendar.MONTH)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = BudgetUiState()
        )

    fun onDateChange(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        _selectedDate.value = calendar
    }
}
