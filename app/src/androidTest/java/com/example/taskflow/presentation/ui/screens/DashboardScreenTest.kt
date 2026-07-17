package com.example.taskflow.presentation.ui.screens

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.taskflow.MainActivity
import com.example.taskflow.di.AppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(AppModule::class)
class DashboardScreenTest{

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compose = createAndroidComposeRule<MainActivity>()

}