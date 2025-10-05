package com.project.budgettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.project.budgettracker.ui.HomeScreen
import com.project.budgettracker.ui.components.AppBar
import com.project.budgettracker.ui.components.NavigationMenu
import com.project.budgettracker.ui.theme.BudgetTrackerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.budgettracker.ui.navigation.Routes
import com.project.budgettracker.ui.screens.AddScreen
import com.project.budgettracker.ui.screens.CategoriesScreen
import com.project.budgettracker.ui.screens.HistoryScreen
import com.project.budgettracker.ui.screens.SummaryScreen
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
                                }
                            )
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
                            composable(Routes.CATEGORIES) { CategoriesScreen() }
                            composable(Routes.ADD) { AddScreen() }
                        }
                    }
                }
            }
        }
    }
}