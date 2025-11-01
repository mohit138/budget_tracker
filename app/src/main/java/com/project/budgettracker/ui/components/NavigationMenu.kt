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
import com.project.budgettracker.ui.navigation.Routes
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

            NavItem(text = "Home") { navigateTo(Routes.HOME) }
            NavItem(text = "Summary") { navigateTo(Routes.SUMMARY) }
            NavItem(text = "History") { navigateTo(Routes.HISTORY) }
            NavItem(text = "Categories") { navigateTo(Routes.CATEGORIES) }
            NavItem(text = "Add Expense") { navigateTo(Routes.ADD_EXPENSE) }
            NavItem(text = "Add Category") { navigateTo(Routes.ADD_CATEGORY) }
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
