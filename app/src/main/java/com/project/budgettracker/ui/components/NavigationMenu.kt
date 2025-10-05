package com.project.budgettracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.budgettracker.ui.navigation.Routes

@Composable
fun NavigationMenu(
    navController: NavController,
    onCloseDrawer: () -> Unit,
    navigateTo: (String) -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Home", modifier = Modifier.clickable { navigateTo(Routes.HOME) })
            Text(text = "Summary", modifier = Modifier.clickable { navigateTo(Routes.SUMMARY) })
            Text(text = "History", modifier = Modifier.clickable { navigateTo(Routes.HISTORY) })
            Text(text = "Categories", modifier = Modifier.clickable { navigateTo(Routes.CATEGORIES) })
            Text(text = "Add Expense", modifier = Modifier.clickable { navigateTo(Routes.ADD) })
            Text(text = "Add Category", modifier = Modifier.clickable { navigateTo(Routes.ADD) })
            Text(text = "Edit Category", modifier = Modifier.clickable { navigateTo(Routes.ADD) })
        }
    }
}