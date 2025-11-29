package com.project.budgettracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
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
    budget: Double,
    isHighlighted: Boolean = false
) {
    val categoryStyle = if (isHighlighted) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleLarge
    val amountStyle = if (isHighlighted) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge

    val isBudgetExceeded = if (budget != 0.0) amountSpent > budget else false
    val progressIndicatorColor = if (isBudgetExceeded) MaterialTheme.colorScheme.error else ProgressIndicatorDefaults.circularColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = category, style = categoryStyle)
            Text(
                text = "₹%.2f / ₹%.2f".format(amountSpent, budget),
                style = amountStyle
            )
        }
        CircularProgressIndicator(
        progress = { (amountSpent / budget).toFloat() },
            color = progressIndicatorColor,
        strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth,
        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )
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

@Preview(showBackground = true)
@Composable
fun HighlightedBudgetSpentRowPreview() {
    BudgetTrackerTheme {
        BudgetSpentRow(
            category = "Gross Budget",
            amountSpent = 45000.0,
            budget = 60000.0,
            isHighlighted = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetSpentRowWithExceededBudgetPreview() {
    BudgetTrackerTheme {
        BudgetSpentRow(
            category = "Gross Budget",
            amountSpent = 65000.0,
            budget = 60000.0
        )
    }
}
