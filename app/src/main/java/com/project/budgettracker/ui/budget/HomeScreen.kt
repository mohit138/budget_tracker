package com.project.budgettracker.ui.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import com.project.budgettracker.ui.components.History
import com.project.budgettracker.ui.components.MonthSelection
import com.project.budgettracker.ui.components.Summary
import com.project.budgettracker.ui.navigation.NavigationDestination
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: BudgetViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    // The data is now pre-computed in the ViewModel. We just use it here.
    val summaryDataForUI = listOfNotNull(uiState.netBudgetData) + uiState.summaryData

    val recentExpenses = uiState.expensesWithCategory

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MonthSelection(
            selectedYear = uiState.selectedYear,
            selectedMonth = uiState.selectedMonth,
            onDateChange = viewModel::onDateChange
        )

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Summary(summaryRowData = summaryDataForUI)
            }
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "History",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp)
                )
                History(
                    expenses = recentExpenses
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BudgetTrackerTheme {
        HomeScreen()
    }
}
