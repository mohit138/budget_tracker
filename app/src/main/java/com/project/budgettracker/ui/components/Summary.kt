package com.project.budgettracker.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun Summary(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            BudgetSpentRow(category = "Groceries", amountSpent = 1200.0, budget = 2000.0)
        }
        item {
            BudgetSpentRow(category = "Entertainment", amountSpent = 800.0, budget = 1500.0)
        }
        item {
            BudgetSpentRow(category = "Utilities", amountSpent = 2500.0, budget = 2500.0)
        }
        item {
            BudgetSpentRow(category = "Groceries", amountSpent = 1200.0, budget = 2000.0)
        }
        item {
            BudgetSpentRow(category = "Entertainment", amountSpent = 800.0, budget = 1500.0)
        }
        item {
            BudgetSpentRow(category = "Utilities", amountSpent = 2500.0, budget = 2500.0)
        }
        item {
            BudgetSpentRow(category = "Groceries", amountSpent = 1200.0, budget = 2000.0)
        }
        item {
            BudgetSpentRow(category = "Entertainment", amountSpent = 800.0, budget = 1500.0)
        }
        item {
            BudgetSpentRow(category = "Utilities", amountSpent = 2500.0, budget = 2500.0)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryPreview() {
    BudgetTrackerTheme {
        Summary()
    }
}