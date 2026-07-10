package com.example.taskflow.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.taskflow.domain.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task")
    fun getTasks() : Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY completedAt ASC")
    fun getTasksByCompletedAt() : Flow<List<Task>>

}