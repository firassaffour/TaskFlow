package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.models.Task
import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) = taskRepository.deleteTask(task)
}