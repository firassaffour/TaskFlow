package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.models.Task
import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleCompleteUseCase @Inject constructor(private val taskRepository: TaskRepository){
    suspend operator fun invoke(task: Task) = taskRepository.toggleComplete(task)
}