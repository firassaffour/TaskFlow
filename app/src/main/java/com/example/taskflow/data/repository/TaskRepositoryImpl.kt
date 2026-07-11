package com.example.taskflow.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.taskflow.data.dao.TaskDAO
import com.example.taskflow.domain.models.Task
import com.example.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class TaskRepositoryImpl(
    private val dao: TaskDAO
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> = dao.getTasks()

    override fun getTasksByCompletedAt(): Flow<List<Task>> = dao.getTasksByCompletedAt()

    override suspend fun upsertTask(task: Task) = dao.upsertTask(task)

    override suspend fun deleteTask(task: Task) = dao.deleteTask(task)

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun toggleComplete(task: Task) {
        val nowCompleted = !task.isCompleted
        dao.upsertTask(
            task.copy(
                isCompleted = nowCompleted,
                completedAt = if (nowCompleted) LocalDateTime.now() else null
            )
        )
    }
}
