package com.project.budgettracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.budgettracker.ui.components.History
import com.project.budgettracker.ui.components.Summary
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

import com.project.budgettracker.ui.components.MonthSelection

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Budget Tracker",
            style = MaterialTheme.typography.headlineLarge
        )
        MonthSelection()
        Summary()
        History()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BudgetTrackerTheme {
        HomeScreen()
    }
}