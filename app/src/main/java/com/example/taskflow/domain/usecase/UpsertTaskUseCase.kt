package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.models.Task
import com.example.taskflow.domain.repository.TaskRepository
import javax.inject.Inject
import kotlin.jvm.Throws

class UpsertTaskUseCase @Inject constructor(private val taskRepository: TaskRepository){

    @Throws(Exception::class)
    suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) throw Exception("Task title cannot be blank")
        else taskRepository.upsertTask(task)
    }
}