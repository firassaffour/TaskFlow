package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke() = taskRepository.getTasks()
}