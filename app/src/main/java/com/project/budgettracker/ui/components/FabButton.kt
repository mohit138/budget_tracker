package com.project.budgettracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun FabButton(onFabClick: () -> Unit) {
    FloatingActionButton(onClick = onFabClick) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Expense")
    }
}