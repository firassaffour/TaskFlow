package com.example.taskflow.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.models.Task
import com.example.taskflow.domain.usecase.DeleteTaskUseCase
import com.example.taskflow.domain.usecase.GetCurrentStreakUseCase
import com.example.taskflow.domain.usecase.GetTasksUseCase
import com.example.taskflow.domain.usecase.ToggleCompleteUseCase
import com.example.taskflow.domain.usecase.UpsertTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val toggleCompleteUseCase: ToggleCompleteUseCase,
    private val getCurrentStreakUseCase: GetCurrentStreakUseCase
) : ViewModel() {

    val tasks = getTasksUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @RequiresApi(Build.VERSION_CODES.O)
    val currentStreak = getCurrentStreakUseCase()
        .map { completedTasks ->
            val completionDates = completedTasks
                .mapNotNull { it.completedAt?.toLocalDate() }
                .distinct()
            AnalyticsEngine.currentStreak(completionDates, LocalDate.now())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


    fun addTask(task: Task){
        viewModelScope.launch {
            try {
                upsertTaskUseCase(task)
            }
            catch (e : Exception){
                Log.e("ViewModel", "addTask error: ${e.message}" )
            }
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            try {
                deleteTaskUseCase(task)
            }
            catch (e : Exception){
                Log.e("ViewModel", "deleteTask error: ${e.message}" )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            try {
                toggleCompleteUseCase(task)
            } catch (e: Exception) {
                Log.e("ViewModel", "toggleComplete error: ${e.message}")
            }
        }
    }
}