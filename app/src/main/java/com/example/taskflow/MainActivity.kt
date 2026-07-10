package com.example.taskflow

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import com.example.taskflow.presentation.ui.theme.TaskFlowTheme
import com.example.taskflow.presentation.ui.MainScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.taskflow.data.room.TaskDatabase
import com.example.taskflow.presentation.viewmodel.TaskViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "tasks.db"
        ).build()
    }

    private val taskViewModel by viewModels<TaskViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(database.dao) as T
                }
            }
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskFlowTheme(darkTheme = false) {
                val systemUiController = rememberSystemUiController()
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                SideEffect {
                    systemUiController.setStatusBarColor(Color.White)
                    systemUiController.setNavigationBarColor(Color.White)
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(navController, currentRoute, taskViewModel)
                }
            }
        }
    }
}

