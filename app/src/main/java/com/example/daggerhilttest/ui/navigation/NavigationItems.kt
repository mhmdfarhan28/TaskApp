package com.example.daggerhilttest.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItems(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tasksCount: Int? = null,
    val completedTask: FilterCriteria
)

val filteredMenuItems = listOf(
    MenuItems(
        title = "All tasks",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home,
        tasksCount = 0,
        completedTask = FilterCriteria.SHOW_ALL
    ),
    MenuItems(
        title = "To-do tasks",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home,
        tasksCount = 0,
        completedTask = FilterCriteria.SHOW_NOT_COMPLETED
    ),
    MenuItems(
        title = "Completed tasks",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home,
        tasksCount = 0,
        completedTask = FilterCriteria.SHOW_COMPLETED
    )

)