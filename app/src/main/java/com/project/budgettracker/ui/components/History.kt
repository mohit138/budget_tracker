package com.project.budgettracker.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun History(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            ExpenseRow(category = "Groceries", amount = 150.0, date = "Oct 24")
        }
        item {
            ExpenseRow(category = "Entertainment", amount = 75.50, date = "Oct 23")
        }
        item {
            ExpenseRow(category = "Utilities", amount = 220.0, date = "Oct 22")
        }
        item {
            ExpenseRow(category = "Groceries", amount = 150.0, date = "Oct 24")
        }
        item {
            ExpenseRow(category = "Entertainment", amount = 75.50, date = "Oct 23")
        }
        item {
            ExpenseRow(category = "Utilities", amount = 220.0, date = "Oct 22")
        }
        item {
            ExpenseRow(category = "Groceries", amount = 150.0, date = "Oct 24")
        }
        item {
            ExpenseRow(category = "Entertainment", amount = 75.50, date = "Oct 23")
        }
        item {
            ExpenseRow(category = "Utilities", amount = 220.0, date = "Oct 22")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryPreview() {
    BudgetTrackerTheme {
        History()
    }
}