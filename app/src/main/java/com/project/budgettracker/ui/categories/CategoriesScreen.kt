package com.project.budgettracker.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.budgettracker.AppViewModelProvider
import com.project.budgettracker.data.entities.Category
import com.project.budgettracker.ui.components.CategoryItem
import com.project.budgettracker.ui.navigation.NavigationDestination
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

object CategoriesDestination : NavigationDestination {
    override val route = "categories"
}

@Composable
fun CategoriesScreen(
    navigateToEditCategory: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val categories by viewModel.categories.collectAsState()

    Column {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        CategoriesList(categories = categories, navigateToEditCategory = navigateToEditCategory)
    }
}

@Composable()
fun CategoriesList(
    categories: List<Category>,
    navigateToEditCategory: (Int) -> Unit
){
    LazyColumn {
        items(items = categories, key = { it.id }) { category ->
            CategoryItem(categoryName = category.name, budget = category.budget, onEditClick = {
                navigateToEditCategory(category.id)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesListPreview() {
    BudgetTrackerTheme {
        CategoriesList(categories = listOf(
            Category(1, "Groceries", 5000.0),
            Category(2, "Entertainment", 3000.0),
            Category(3, "Utilities", 4000.0),
            Category(4, "Investment", 5000.0),
        ), navigateToEditCategory = {})
    }
}
