package com.project.budgettracker.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.project.budgettracker.ui.components.CategoryItem
import com.project.budgettracker.ui.navigation.NavigationDestination
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

object CategoriesDestination : NavigationDestination {
    override val route = "categories"
}

@Composable
fun CategoriesScreen(navController: NavController) {
    Column {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineLarge
        )
        CategoryItem(categoryName = "Groceries", budget = 5000.0, onEditClick = {
            navController.navigate(EditCategoryDestination.route)
        })
        CategoryItem(categoryName = "Entertainment", budget = 3000.0, onEditClick = {
            navController.navigate(EditCategoryDestination.route)
        })
        CategoryItem(categoryName = "Utilities", budget = 4000.0, onEditClick = {
            navController.navigate(EditCategoryDestination.route)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    BudgetTrackerTheme {
        // You can create a mock NavController for the preview if needed
         CategoriesScreen(navController = NavController(LocalContext.current))
    }
}
