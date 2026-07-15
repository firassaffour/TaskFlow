package com.example.taskflow.data.repository

import com.example.taskflow.domain.models.Task
import com.example.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTaskRepository : TaskRepository {

    private val tasks = mutableListOf<Task>()

    override fun getTasks(): Flow<List<Task>> {
       return flow { emit(tasks) }
    }

    override fun getTasksByCompletedAt(): Flow<List<Task>> {
        return flow { emit(tasks.filter { it.completedAt != null } ) }
    }

    override suspend fun upsertTask(task: Task) {
        tasks.add(task)
    }

    override suspend fun deleteTask(task: Task) {
        tasks.remove(task)
    }

    override suspend fun toggleComplete(task: Task) {
        tasks.map {
            if (it.id == task.id) {
                it.copy(isCompleted = !it.isCompleted)
            } else it
        }
    }
}