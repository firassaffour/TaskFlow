package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject

class GetCurrentStreakUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke() = taskRepository.getTasksByCompletedAt()
}