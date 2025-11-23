package com.project.budgettracker.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.project.budgettracker.ui.budget.ExpenseWithCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun History(
    expenses: List<ExpenseWithCategory>,
    isDetailedHistory: Boolean = false
) {
    LazyColumn {
        items(expenses) { (expense, category) ->
            ExpenseRow(
                category = category?.name ?: "Uncategorized",
                amount = expense.amount,
                date = expense.date.time.toFormattedDateString(),
                description = expense.description,
                showDetailedDescription = isDetailedHistory
            )
        }
    }
}

fun Long.toFormattedDateString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(date)
}
