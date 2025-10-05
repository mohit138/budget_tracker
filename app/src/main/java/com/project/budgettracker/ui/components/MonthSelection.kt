package com.project.budgettracker.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthSelection() {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)

    val years = (currentYear - 5..currentYear).toList()
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var expandedYear by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    var expandedMonth by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(months[currentMonth]) }

    Row(modifier = Modifier.padding(16.dp)) {
        ExposedDropdownMenuBox(
            expanded = expandedMonth,
            onExpandedChange = { expandedMonth = !expandedMonth },
            modifier = Modifier.weight(0.6f).padding(end = 8.dp)
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedMonth,
                onValueChange = {},
                label = { Text("Month") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expandedMonth,
                onDismissRequest = { expandedMonth = false }
            ) {
                months.forEachIndexed { index, month ->
                    val enabled = selectedYear != currentYear || index <= currentMonth
                    DropdownMenuItem(
                        text = { Text(month) },
                        onClick = {
                            selectedMonth = month
                            expandedMonth = false
                        },
                        enabled = enabled
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = expandedYear,
            onExpandedChange = { expandedYear = !expandedYear },
            modifier = Modifier.weight(0.4f)
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedYear.toString(),
                onValueChange = {},
                label = { Text("Year") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expandedYear,
                onDismissRequest = { expandedYear = false }
            ) {
                years.forEach { year ->
                    DropdownMenuItem(
                        text = { Text(year.toString()) },
                        onClick = {
                            selectedYear = year
                            expandedYear = false
                        }
                    )
                }
            }
        }
    }
}