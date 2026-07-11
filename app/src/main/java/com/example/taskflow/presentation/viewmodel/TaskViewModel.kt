package com.example.taskflow.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.models.Task
import com.example.taskflow.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {

    val tasks = taskRepository.getTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun addTask(task: Task){
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(task)
            }
            catch (e : Exception){
                Log.e("ViewModel", "addTask error: ${e.message}", )
            }
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(task)
            }
            catch (e : Exception){
                Log.e("ViewModel", "deleteTask error: ${e.message}", )
            }
        }
    }
}