package com.project.budgettracker

import android.content.pm.PackageManager
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.budgettracker.ui.add_expense.AddExpenseDestination
import com.project.budgettracker.ui.add_expense.AddExpenseScreen
import com.project.budgettracker.ui.budget.HistoryDestination
import com.project.budgettracker.ui.budget.HistoryScreen
import com.project.budgettracker.ui.budget.HomeDestination
import com.project.budgettracker.ui.budget.HomeScreen
import com.project.budgettracker.ui.budget.SummaryDestination
import com.project.budgettracker.ui.budget.SummaryScreen
import com.project.budgettracker.ui.categories.AddCategoryDestination
import com.project.budgettracker.ui.categories.AddCategoryScreen
import com.project.budgettracker.ui.categories.CategoriesDestination
import com.project.budgettracker.ui.categories.CategoriesScreen
import com.project.budgettracker.ui.categories.EditCategoryDestination
import com.project.budgettracker.ui.categories.EditCategoryScreen
import com.project.budgettracker.ui.components.AppBar
import com.project.budgettracker.ui.components.NavigationMenu
import com.project.budgettracker.ui.theme.BudgetTrackerTheme
import kotlinx.coroutines.launch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // user granted — optionally show a toast
        } else {
            // user denied — inform them the feature won't show notifications
        }
    }

    // call this function when you want to request permission
    private fun ensurePostNotificationsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val perm = android.Manifest.permission.POST_NOTIFICATIONS
            when {
                ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED -> {
                    // already granted
                }
                shouldShowRequestPermissionRationale(perm) -> {
                    // optionally show rationale and then request
                    requestNotificationPermissionLauncher.launch(perm)
                }
                else -> {
                    requestNotificationPermissionLauncher.launch(perm)
                }
            }
        }
    }

    private fun createExpenseNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                CHANNEL_ID,
                "Expense detection",
                android.app.NotificationManager.IMPORTANCE_HIGH // heads-up
            ).apply {
                description = "Notifications when the app detects a possible expense"
            }
            val nm = getSystemService(android.app.NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "expense_detection_channel"
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        createExpenseNotificationChannel()
        ensurePostNotificationsPermission()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetTrackerTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val startIntent = intent
                LaunchedEffect(startIntent) {
                    val navigateTo = startIntent?.getStringExtra("navigate_to")
                    val amount = startIntent?.getDoubleExtra("expense_amount", -1.0)
                    if (navigateTo == "add_expense" && amount != null && amount != -1.0) {
                        navController.navigate("${AddExpenseDestination.route}?amount=$amount")
                    }
                }

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
                                    navController.navigate(HomeDestination.route)
                                }
                            )
                        },
                        floatingActionButton = {
                            if (currentRoute in listOf(HomeDestination.route, SummaryDestination.route, HistoryDestination.route)) {
                                FloatingActionButton(
                                    onClick = { navController.navigate(AddExpenseDestination.route) }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = HomeDestination.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(HomeDestination.route) { HomeScreen() }
                            composable(SummaryDestination.route) { SummaryScreen() }
                            composable(HistoryDestination.route) { HistoryScreen() }
                            composable(CategoriesDestination.route) {
                                CategoriesScreen(navigateToEditCategory = { categoryId ->
                                    navController.navigate("${EditCategoryDestination.route}/$categoryId")
                                })
                            }
                            composable(
                                route = AddExpenseDestination.routeWithArgs,
                                arguments = listOf(
                                    navArgument(AddExpenseDestination.amountArg) {
                                        type = NavType.StringType
                                        nullable = true   // IMPORTANT — makes it optional
                                        defaultValue = null
                                    }
                                )
                            ) {
                                val amount = it.arguments?.getString(AddExpenseDestination.amountArg)
                                AddExpenseScreen(
                                    navigateToHome = { navController.navigate(HomeDestination.route) },
                                    prefilledAmount = amount
                                )
                            }
                            composable(AddCategoryDestination.route) { AddCategoryScreen(
                                navigateToCategoriesScreen = { navController.navigate(CategoriesDestination.route) }
                            ) }
                            composable(
                                route = EditCategoryDestination.routeWithArgs,
                                arguments = listOf(navArgument(EditCategoryDestination.categoryIdArg) {
                                    type = NavType.IntType
                                })
                                ) { EditCategoryScreen(
                                    navigateBack = { navController.navigateUp() }
                                ) }
                        }
                    }
                }
            }
        }
    }
}
