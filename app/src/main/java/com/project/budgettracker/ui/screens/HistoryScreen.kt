package com.project.budgettracker.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.budgettracker.ui.components.History
import com.project.budgettracker.ui.components.MonthSelection
import com.project.budgettracker.ui.components.Summary
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun HistoryScreen() {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        MonthSelection()
        History()
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    BudgetTrackerTheme {
        HistoryScreen()
    }
}
