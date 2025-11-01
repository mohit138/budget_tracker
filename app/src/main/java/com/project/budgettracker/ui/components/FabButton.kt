package com.project.budgettracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun FabButton(onFabClick: () -> Unit) {
    FloatingActionButton(onClick = onFabClick) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Expense")
    }
}

@Preview(showBackground = true)
@Composable
fun FabButtonPreview() {
    BudgetTrackerTheme {
        FabButton(onFabClick = {})
    }
}