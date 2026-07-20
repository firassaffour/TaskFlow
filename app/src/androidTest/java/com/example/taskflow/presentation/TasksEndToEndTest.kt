package com.example.taskflow.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.taskflow.MainActivity
import com.example.taskflow.di.AppModule
import com.example.taskflow.domain.models.Priority
import com.example.taskflow.domain.models.ProjectType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class TasksEndToEndTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compose = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun upsertNewTest_toggleCompleteAfterwards(){

        compose.onNodeWithContentDescription("Add task").performClick()

        compose.onNodeWithTag("TITLE_FIELD").performTextInput("Test Task")
        compose.onNodeWithTag("DESCRIPTION_FIELD").performTextInput("Test Description")
        compose.onNodeWithTag(Priority.HIGH.label).performClick()
        compose.onNodeWithTag(ProjectType.PERSONAL.label).performClick()

        compose.onNodeWithTag("ADD_TASK_BUTTON").performClick()

        compose.onNodeWithTag("Check_box").performClick()

    }
}