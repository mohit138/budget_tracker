package com.project.budgettracker.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Column
import com.project.budgettracker.ui.components.History
import com.project.budgettracker.ui.components.MonthSelection

@Composable
fun HistoryScreen() {
    Column {
        MonthSelection()
        History()
    }
}
