package com.example.taskflow.presentation.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.MainActivity
import com.example.taskflow.di.AppModule
import com.example.taskflow.presentation.ui.theme.TaskFlowTheme
import com.example.taskflow.presentation.viewmodel.ProfileViewModel
import com.example.taskflow.presentation.viewmodel.TaskViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class DashboardScreenTest{

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compose = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()
    }


    @Test
    fun clickComplete_updatesTask(){
        compose.onNodeWithTag("Good Morning,").performClick()
        compose.onNodeWithTag("Good Morning,").assertIsDisplayed()
    }

}