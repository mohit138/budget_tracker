package com.project.budgettracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun BudgetSpentRow(
    category: String,
    amountSpent: Double,
    budget: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = category, style = MaterialTheme.typography.titleLarge)
            Text(
                text = "₹%.2f / ₹%.2f".format(amountSpent, budget),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        CircularProgressIndicator(progress = (amountSpent / budget).toFloat())
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetSpentRowPreview() {
    BudgetTrackerTheme {
        BudgetSpentRow(
            category = "Groceries",
            amountSpent = 1500.0,
            budget = 5000.0
        )
    }
}
