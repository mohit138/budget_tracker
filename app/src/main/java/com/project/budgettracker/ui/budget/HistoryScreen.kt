package com.project.budgettracker.ui.budget

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.budgettracker.ui.AppViewModelProvider
import com.project.budgettracker.ui.components.History
import com.project.budgettracker.ui.components.MonthSelection
import com.project.budgettracker.ui.navigation.NavigationDestination
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

object HistoryDestination : NavigationDestination {
    override val route = "history"
}

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val uiState by viewModel.uiState.collectAsState()
    Log.d("HistoryScreen", "uiState: $uiState")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        MonthSelection(
            selectedYear = uiState.selectedYear,
            selectedMonth = uiState.selectedMonth,
            onDateChange = viewModel::onDateChange
        )
        History(expenses = uiState.expenses)
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    BudgetTrackerTheme {
        HistoryScreen()
    }
}
