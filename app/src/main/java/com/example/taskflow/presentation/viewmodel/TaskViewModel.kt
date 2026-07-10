package com.example.taskflow.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.dao.TaskDAO
import com.example.taskflow.domain.models.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val dao : TaskDAO) : ViewModel() {

    val tasks = dao.getTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun addTask(task: Task){
        viewModelScope.launch {
            try {
                dao.upsertTask(task)
            }
            catch (e : Exception){
                Log.e("ViewModel", "addTask error: ${e.message}", )
            }
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            try {
                dao.deleteTask(task)
            }
            catch (e : Exception){
                Log.e("ViewModel", "deleteTask error: ${e.message}", )
            }
        }
    }
}