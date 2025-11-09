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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.budgettracker.AppViewModelProvider
import com.project.budgettracker.ui.navigation.NavigationDestination
import com.project.budgettracker.ui.theme.BudgetTrackerTheme
import kotlinx.coroutines.launch

object EditCategoryDestination : NavigationDestination {
    override val route = "edit_category"
    const val categoryIdArg = "categoryId"
    val routeWithArgs = "$route/{$categoryIdArg}"
}

@Composable
fun EditCategoryScreen(
    viewModel: EditCategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val categoryDetails = viewModel.categoryUiState.categoryDetails

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Edit Category", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = categoryDetails.name,
            onValueChange = { viewModel.updateCategoryUiState(categoryDetails.copy(name = it)) },
            label = { Text("Category Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = categoryDetails.budget,
            onValueChange = { viewModel.updateCategoryUiState(categoryDetails.copy(budget = it)) },
            label = { Text("Budget Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            coroutineScope.launch {
                viewModel.updateCategory()
                navigateBack()
            }
        }) {
            Text("Update Category")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditCategoryScreenPreview() {
    BudgetTrackerTheme {
        EditCategoryScreen(
            viewModel = viewModel(factory = AppViewModelProvider.Factory),
            navigateBack = {}
        )
    }
}
