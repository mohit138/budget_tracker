package com.project.budgettracker.ui.budget

import com.project.budgettracker.data.entities.Category
import com.project.budgettracker.data.entities.Expense
import com.project.budgettracker.data.repositories.CategoryRepository
import com.project.budgettracker.data.repositories.ExpensesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Date

@ExperimentalCoroutinesApi
class BudgetViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: BudgetViewModel
    private lateinit var expensesRepository: FakeExpensesRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        expensesRepository = FakeExpensesRepository()
        viewModel = BudgetViewModel(
            expensesRepository,
            FakeCategoryRepository()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val uiState = viewModel.uiState.first { it.expensesWithCategory.size == 4 }
        assertEquals(2, uiState.allCategories.size)
        assertEquals(expensesRepository.currentMonthTotal, uiState.totalExpenses, 0.0)
        assertEquals(2, uiState.summaryData.size)
    }

    @Test
    fun `onDateChange updates state`() = runTest {
        // Wait for initial state to load
        viewModel.uiState.first { it.expensesWithCategory.size == 4 }

        // Change date to previous month
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        viewModel.onDateChange(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))

        // Wait for state to update to previous month's expenses
        val updatedUiState = viewModel.uiState.first { it.totalExpenses == expensesRepository.prevMonthTotal }

        assertEquals(1, updatedUiState.summaryData.size)
    }

    @Test
    fun `expenses on first and last day of month are fetched`() = runTest {
        // The fake repository now contains expenses on the 1st and last day of the month
        // The viewmodel is initialized with the current date by default.
        // We need to check if those expenses are fetched correctly on initial load.

        val uiState = viewModel.uiState.first { it.expensesWithCategory.size == 4 }

        val expenseDescriptions = uiState.expensesWithCategory.map { it.expense.description }

        // Assert that all current month expenses are present
        assertEquals(4, uiState.expensesWithCategory.size)
        assertTrue(expenseDescriptions.contains("Groceries"))
        assertTrue(expenseDescriptions.contains("Movies"))
        assertTrue(expenseDescriptions.contains("Coffee"))    // first day
        assertTrue(expenseDescriptions.contains("Dinner"))    // last day
    }

    @Test
    fun `when date is changed to 1st of month, expenses from earlier that day are included`() = runTest {
        // The FakeExpensesRepository has an expense "Coffee" on the 1st of the month at 1 AM.

        // 1. Simulate a date change to the 1st of the current month.
        val cal = Calendar.getInstance()
        viewModel.onDateChange(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))

        // 2. Wait for the state to update. The total number of expenses for the current month is 4.
        val updatedUiState = viewModel.uiState.first { it.expensesWithCategory.size == 4 }

        // 3. Assert that the expense from 1 AM is present in the state.
        val expenseDescriptions = updatedUiState.expensesWithCategory.map { it.expense.description }
        assertTrue(
            "Expense from early morning on 1st of month should be included",
            expenseDescriptions.contains("Coffee")
        )
    }
}

class FakeExpensesRepository : ExpensesRepository {
    val expenses: List<Expense>
    val currentMonthTotal: Double
    val prevMonthTotal: Double

    init {
        val cal = Calendar.getInstance()

        // Current month expenses
        cal.set(Calendar.DAY_OF_MONTH, 10)
        val e1 = Expense(1, 1, 50.0, "Groceries", Date(cal.timeInMillis))
        cal.set(Calendar.DAY_OF_MONTH, 15)
        val e2 = Expense(2, 2, 75.0, "Movies", Date(cal.timeInMillis))

        // Previous month expenses
        val prevMonthCal = Calendar.getInstance()
        prevMonthCal.add(Calendar.MONTH, -1)
        prevMonthCal.set(Calendar.DAY_OF_MONTH, 5)
        val e3 = Expense(3, 1, 100.0, "Clothes", Date(prevMonthCal.timeInMillis))
        prevMonthTotal = e3.amount // 100.0

        // New expense at start of month
        cal.time = Date() // Reset to current time
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 1)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        val e4 = Expense(4, 1, 25.0, "Coffee", Date(cal.timeInMillis))

        // New expense at end of month
        cal.time = Date() // Reset to current time
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        val e5 = Expense(5, 2, 80.0, "Dinner", Date(cal.timeInMillis))

        currentMonthTotal = e1.amount + e2.amount + e4.amount + e5.amount

        expenses = listOf(e1, e2, e3, e4, e5)
    }

    override fun getExpensesBetweenDates(startDate: Date, endDate: Date): Flow<List<Expense>> {
        val filteredExpenses = expenses.filter {
            val expenseTime = it.date.time
            // The range is inclusive [startDate, endDate]
            expenseTime >= startDate.time && expenseTime <= endDate.time
        }
        return flowOf(filteredExpenses)
    }

    override fun getAllExpenses(): Flow<List<Expense>> = flowOf(expenses)

    override fun getExpensesForCategory(categoryId: Int): Flow<List<Expense>> {
        return flowOf(expenses.filter { it.categoryId == categoryId })
    }

    override suspend fun insertExpense(expense: Expense) {}
    override suspend fun deleteExpense(expense: Expense) {}
    override suspend fun updateExpense(expense: Expense) {}
}

class FakeCategoryRepository : CategoryRepository {
    private val categories = MutableStateFlow(
        listOf(
            Category(1, "Groceries", 500.0),
            Category(2, "Entertainment", 200.0)
        )
    )

    override fun getAllCategories(): Flow<List<Category>> = categories
    override fun getCategoryById(id: Int): Flow<Category?> {
        return flowOf(categories.value.find { it.id == id })
    }

    override suspend fun insertCategory(category: Category) {
        categories.update { it + category }
    }

    override suspend fun deleteCategory(category: Category) {
        categories.update { it - category }
    }

    override suspend fun updateCategory(category: Category) {
        categories.update { list ->
            list.map { if (it.id == category.id) category else it }
        }
    }
}
