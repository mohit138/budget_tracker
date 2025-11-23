package com.project.budgettracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun ExpenseRow(
    modifier: Modifier = Modifier,
    category: String,
    amount: Double,
    date: String,
    description: String,
    showDetailedDescription: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
//            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
//                Text(
//                    text = " • ",
//                    style = MaterialTheme.typography.bodyLarge
//                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = if (showDetailedDescription) 2 else 1,
                    overflow = TextOverflow.Ellipsis
                )
//            }

        }
        Text(
            text = "₹${"%.2f".format(amount)}",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseRowPreview() {
    BudgetTrackerTheme {
        ExpenseRow(
            category = "Groceries",
            amount = 1250.75,
            date = "23 Oct 2024",
            description = "A very long description that should overflow and be ellipsized"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseRowDetailedPreview() {
    BudgetTrackerTheme {
        ExpenseRow(
            category = "Groceries",
            amount = 1250.75,
            date = "23 Oct 2024",
            description = "A very long description that should overflow.",
            showDetailedDescription = true
        )
    }
}
