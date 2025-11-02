package com.project.budgettracker.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun AddCategoryScreen() {
    var categoryName by remember { mutableStateOf("") }
    var budgetAmount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Add New Category", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("Category Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = budgetAmount,
            onValueChange = { budgetAmount = it },
            label = { Text("Budget Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { /*TODO*/ }) {
            Text("Add Category")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddCategoryScreenPreview() {
    BudgetTrackerTheme {
        AddCategoryScreen()
    }
}
