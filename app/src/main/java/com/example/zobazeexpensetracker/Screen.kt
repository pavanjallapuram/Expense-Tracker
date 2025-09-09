package com.example.zobazeexpensetracker

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

sealed class Screen(
    val route: String,
    val label: String,
    val activeIcon: Int,
    val inactiveIcon: Int
) {
    object Entry : Screen(
        "entry",
        "Entry",
        R.drawable.ic_home_active,
        R.drawable.ic_home_unactive
    )

    object List : Screen(
        "list",
        "List",
        R.drawable.ic_list_active,
        R.drawable.ic_list_inactive
    )

    object Report : Screen(
        "report",
        "Report",
        R.drawable.ic_report_active,
        R.drawable.ic_report_inactive
    )

    companion object {
        val items = listOf(Entry, List, Report)
    }
}
