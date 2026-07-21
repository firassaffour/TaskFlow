package com.example.taskflow

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.taskflow.presentation.ui.theme.TaskFlowTheme
import com.example.taskflow.presentation.ui.MainScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.taskflow.data.DataStoreManager
import com.example.taskflow.data.ReminderWorker
import com.example.taskflow.presentation.viewmodel.ProfileViewModel
import com.example.taskflow.presentation.viewmodel.TaskViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val dataStoreManager = DataStoreManager(this)
            val darkMode by dataStoreManager.getDarkMode().collectAsState(initial = false)

            TaskFlowTheme(darkTheme = darkMode) {
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                val taskViewModel = hiltViewModel<TaskViewModel>()
                val profileViewModel = hiltViewModel<ProfileViewModel>()

                SideEffect {

                    when(darkMode){
                        true -> {
                            systemUiController.setSystemBarsColor(Color.Black)
                            systemUiController.setNavigationBarColor(Color.Black)
                        }
                        false -> {
                            systemUiController.setSystemBarsColor(Color.White)
                            systemUiController.setNavigationBarColor(Color.White)
                        }
                    }
                }

                val request =
                    PeriodicWorkRequestBuilder<ReminderWorker>(
                        24,
                        TimeUnit.HOURS
                    ).build()

                WorkManager.getInstance(this)
                    .enqueueUniquePeriodicWork(
                        "daily_reminder",
                        ExistingPeriodicWorkPolicy.KEEP,
                        request
                    )

                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(navController, currentRoute, taskViewModel, profileViewModel)
                }
            }
        }
    }
}

