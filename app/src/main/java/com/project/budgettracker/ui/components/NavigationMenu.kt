package com.project.budgettracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.budgettracker.ui.budget.HistoryDestination
import com.project.budgettracker.ui.budget.HomeDestination
import com.project.budgettracker.ui.budget.SummaryDestination
import com.project.budgettracker.ui.categories.AddCategoryDestination
import com.project.budgettracker.ui.categories.CategoriesDestination
import com.project.budgettracker.ui.add_expense.AddExpenseDestination
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@Composable
fun NavigationMenu(
    navController: NavController,
    onCloseDrawer: () -> Unit,
    navigateTo: (String) -> Unit
) {
    ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.8f)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onCloseDrawer) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Navigation Menu"
                    )
                }
            }

            NavItem(text = "Home") { navigateTo(HomeDestination.route) }
            NavItem(text = "Summary") { navigateTo(SummaryDestination.route) }
            NavItem(text = "History") { navigateTo(HistoryDestination.route) }
            NavItem(text = "Categories") { navigateTo(CategoriesDestination.route) }
            NavItem(text = "Add Expense") { navigateTo(AddExpenseDestination.route) }
            NavItem(text = "Add Category") { navigateTo(AddCategoryDestination.route) }
        }
    }
}

@Composable
private fun NavItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun NavigationMenuPreview() {
    BudgetTrackerTheme {
        NavigationMenu(
            onCloseDrawer = {}, navigateTo = {},
            navController = NavController(LocalContext.current)
        )
    }
}
