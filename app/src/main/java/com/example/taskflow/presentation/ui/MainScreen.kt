package com.example.taskflow.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskflow.presentation.ui.screens.AddTaskScreen
import com.example.taskflow.presentation.ui.screens.AnalyticsScreen
import com.example.taskflow.presentation.ui.screens.CalendarScreen
import com.example.taskflow.presentation.ui.screens.DashboardScreen
import com.example.taskflow.presentation.viewmodel.TaskViewModel
import com.example.taskflow.presentation.ui.screens.ProfileScreen
import com.example.taskflow.presentation.viewmodel.ProfileViewModel

private enum class Tab(val label: String) {
    HOME("Home"), CALENDAR("Calendar"), ANALYTICS("Analytics"), PROFILE("Profile"), ADDTASK("addtask")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController : NavHostController, currentRoute : String?, taskViewModel: TaskViewModel, profileViewModel: ProfileViewModel) {
    var selectedTab by remember { mutableStateOf(Tab.HOME) }
    var showAddTask by remember { mutableStateOf(false) }


    Scaffold(
        floatingActionButton = {
            if (currentRoute == Tab.HOME.label) {
                FloatingActionButton(onClick = {
                    navController.navigate(Tab.ADDTASK.label)
                    showAddTask = true},
                    containerColor = MaterialTheme.colorScheme.primary) {
                    Icon(Icons.Filled.Add, contentDescription = "Add task")
                }
            }
        },
        bottomBar = {
            if (currentRoute != Tab.ADDTASK.label) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background) {
                    NavigationBarItem(
                        selected = currentRoute == Tab.HOME.label,
                        onClick = { navController.navigate(Tab.HOME.label){
                            popUpTo(Tab.HOME.label)
                            launchSingleTop = true
                            restoreState = true
                        } },
                        icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                        label = { Text("Home") },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surface)
                    )
                    NavigationBarItem(
                        selected = currentRoute == Tab.CALENDAR.label,
                        onClick = { navController.navigate(Tab.CALENDAR.label){
                            popUpTo(Tab.HOME.label)
                            launchSingleTop = true
                            restoreState = true
                        } },
                        icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = null) },
                        label = { Text("Calendar") },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surface)
                    )
                    NavigationBarItem(
                        selected = currentRoute == Tab.ANALYTICS.label,
                        onClick = { navController.navigate(Tab.ANALYTICS.label){
                            popUpTo(Tab.HOME.label)
                            launchSingleTop = true
                            restoreState = true
                        } },
                        icon = { Icon(Icons.Filled.QueryStats, contentDescription = null) },
                        label = { Text("Analytics") },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surface)
                    )
                    NavigationBarItem(
                        selected = currentRoute == Tab.PROFILE.label,
                        onClick = { navController.navigate(Tab.PROFILE.label){
                            popUpTo(Tab.HOME.label)
                            launchSingleTop = true
                            restoreState = true
                        } },
                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        label = { Text("Profile") },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Tab.HOME.label,
            modifier = Modifier.padding(padding)
        ) {
            composable(Tab.HOME.label) {  DashboardScreen(taskViewModel, profileViewModel) }
            composable(Tab.CALENDAR.label) { CalendarScreen(taskViewModel) }
            composable(Tab.ANALYTICS.label) {  AnalyticsScreen(taskViewModel) }
            composable(Tab.PROFILE.label) {  ProfileScreen(taskViewModel, profileViewModel) }
            composable(Tab.ADDTASK.label) {  AddTaskScreen(onTaskCreated = {navController.popBackStack()}, navController, taskViewModel)}
        }
    }
}
