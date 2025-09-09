package com.example.zobazeexpensetracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.zobazeexpensetracker.data.ExpenseDatabase
import com.example.zobazeexpensetracker.data.RoomExpenseRepository
import com.example.zobazeexpensetracker.screens.ExpenseEntryScreen
import com.example.zobazeexpensetracker.screens.ExpenseListScreen

import com.example.zobazeexpensetracker.screens.FinalReportScreen
import com.example.zobazeexpensetracker.ui.theme.PrimaryColor
import com.example.zobazeexpensetracker.ui.theme.SecondaryColor
import com.example.zobazeexpensetracker.ui.theme.ZobazeExpenseTrackerTheme
import com.example.zobazeexpensetracker.viewmodels.ExpenseViewModel
import com.example.zobazeexpensetracker.viewmodels.ExpenseViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {



    private val rootViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(
            RoomExpenseRepository(
                ExpenseDatabase.getDatabase(applicationContext).expenseDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZobazeExpenseTrackerTheme {





                ExpenseApp(rootViewModel)


            }
        }
    }
}


@Composable
fun ExpenseApp(rootViewModel: ExpenseViewModel) {
    val navController = rememberNavController()
    val state by rootViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope() // ✅ safe here

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Entry.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Entry.route) {
                ExpenseEntryScreen(
                    onAdd = { expense ->
                        scope.launch { rootViewModel.addExpense(expense) }
                    },
                    totalToday = state.totalToday,viewModel = rootViewModel
                )
            }
            composable(Screen.List.route) {

                ExpenseListScreen(
                    expenses = state.expenses,
                    onDelete = { id ->
                        scope.launch { rootViewModel.deleteExpense(id) }
                    },
                    groupByCategory = state.groupByCategory,
                    // 🔹 Toggle grouping (category ↔ time)
                    onToggleGroup = { rootViewModel.toggleGroupByCategory() },
                    // 🔹 Filter by date from DatePicker
                    onFilterDate = { millis ->
                        rootViewModel.setFilterDate(millis)
                    }
                )
            }
            composable(Screen.Report.route) {
                FinalReportScreen(viewModel = rootViewModel)
            }
        }
    }
}


@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
        Screen.items.forEach { screen ->
            val selected = currentDestination?.route == screen.route

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = if (selected) screen.activeIcon else screen.inactiveIcon),
                        contentDescription = screen.label,
                        tint = if (selected) PrimaryColor else SecondaryColor  // optional tint
                    )
                },
                label = { Text(screen.label,
                    color = if (selected) PrimaryColor else SecondaryColor) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}








