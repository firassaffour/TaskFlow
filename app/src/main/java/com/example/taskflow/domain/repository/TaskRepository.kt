package com.example.taskflow.domain.repository

import com.example.taskflow.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTasks(): Flow<List<Task>>

    fun getTasksByCompletedAt(): Flow<List<Task>>

    suspend fun upsertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun toggleComplete(task: Task)
}
