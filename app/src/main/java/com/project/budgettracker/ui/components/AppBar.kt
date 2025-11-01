package com.project.budgettracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.budgettracker.ui.theme.BudgetTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(onNavigationIconClick: () -> Unit, onTitleClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Budget Tracker",
                modifier = Modifier.clickable(onClick = onTitleClick)
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview(){
    BudgetTrackerTheme {
        AppBar(onNavigationIconClick = {}, onTitleClick = {})
    }
}