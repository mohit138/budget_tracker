package com.project.budgettracker.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

/**
 * Data class representing the data for a single row in the Summary list.
 */
data class SummaryRowData(
    val category: String,
    val amountSpent: Double,
    val budget: Double
)

@Composable
fun Summary(modifier: Modifier = Modifier, summaryRowData: List<SummaryRowData>) {
    LazyColumn(modifier = modifier) {
        items(summaryRowData) { data ->
            BudgetSpentRow(
                category = data.category,
                amountSpent = data.amountSpent,
                budget = data.budget,
                isHighlighted = (data.category == "Net Budget") // Example of conditional highlighting
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryPreview() {
    BudgetTrackerTheme {
        val sampleData = listOf(
            SummaryRowData("Net Budget", 4500.0, 6000.0),
            SummaryRowData("Groceries", 1200.0, 2000.0),
            SummaryRowData("Entertainment", 800.0, 1500.0),
            SummaryRowData("Utilities", 2500.0, 2500.0)
        )
        Summary(summaryRowData = sampleData)
    }
}
