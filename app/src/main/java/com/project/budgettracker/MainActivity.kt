package com.project.budgettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.budgettracker.ui.components.AppBar
import com.project.budgettracker.ui.components.NavigationMenu
import com.project.budgettracker.ui.navigation.Routes
import com.project.budgettracker.ui.categories.AddCategoryScreen
import com.project.budgettracker.ui.add_expense.AddExpenseScreen
import com.project.budgettracker.ui.categories.CategoriesScreen
import com.project.budgettracker.ui.categories.EditCategoryScreen
import com.project.budgettracker.ui.budget.HistoryScreen
import com.project.budgettracker.ui.budget.HomeScreen
import com.project.budgettracker.ui.budget.SummaryScreen
import com.project.budgettracker.ui.theme.BudgetTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetTrackerTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        NavigationMenu(
                            navController = navController,
                            onCloseDrawer = {
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            navigateTo = { route ->
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(route)
                            }
                        )
                    }
                ) {
                    Scaffold(
                        topBar = {
                            AppBar(
                                onNavigationIconClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                                onTitleClick = {
                                    navController.navigate(Routes.HOME)
                                }
                            )
                        },
                        floatingActionButton = {
                            if (currentRoute in listOf(Routes.HOME, Routes.SUMMARY, Routes.HISTORY)) {
                                FloatingActionButton(
                                    onClick = { navController.navigate(Routes.ADD_EXPENSE) }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Routes.HOME,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Routes.HOME) { HomeScreen() }
                            composable(Routes.SUMMARY) { SummaryScreen() }
                            composable(Routes.HISTORY) { HistoryScreen() }
                            composable(Routes.CATEGORIES) { CategoriesScreen(navController = navController) }
                            composable(Routes.ADD_EXPENSE) { AddExpenseScreen() }
                            composable(Routes.ADD_CATEGORY) { AddCategoryScreen() }
                            composable(Routes.EDIT_CATEGORY) { EditCategoryScreen() }
                        }
                    }
                }
            }
        }
    }
}
