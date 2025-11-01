package com.project.budgettracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun CategoryItem(
    categoryName: String,
    budget: Double,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = categoryName, style = MaterialTheme.typography.titleLarge)
            Text(text = "Budget: ₹%.2f".format(budget), style = MaterialTheme.typography.titleLarge)
        }
        IconButton(onClick = onEditClick) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Category")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    BudgetTrackerTheme {
        CategoryItem(
            categoryName = "Groceries",
            budget = 5000.0,
            onEditClick = {}
        )
    }
}
