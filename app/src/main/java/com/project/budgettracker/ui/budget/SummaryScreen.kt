package com.project.budgettracker.ui.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.budgettracker.ui.AppViewModelProvider
import com.project.budgettracker.ui.components.MonthSelection
import com.project.budgettracker.ui.components.Summary
import com.project.budgettracker.ui.navigation.NavigationDestination
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

object SummaryDestination : NavigationDestination {
    override val route = "summary"
}

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: BudgetViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    // The data is now pre-computed in the ViewModel. We just use it here.
    val finalSummaryData = listOfNotNull(uiState.netBudgetData) + uiState.summaryData

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        MonthSelection(
            selectedYear = uiState.selectedYear,
            selectedMonth = uiState.selectedMonth,
            onDateChange = viewModel::onDateChange
        )
        Summary(summaryRowData = finalSummaryData)
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    BudgetTrackerTheme {
        SummaryScreen(
            viewModel = viewModel(factory = AppViewModelProvider.Factory)
        )
    }
}
