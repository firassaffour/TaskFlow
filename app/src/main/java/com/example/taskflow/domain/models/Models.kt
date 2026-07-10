package com.example.taskflow.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

enum class Priority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}

enum class ProjectType(val label: String) {
    WORK("Work"),
    PERSONAL("Personal"),
    SHOPPING("Shopping")
}

/**
 * A single task. [completedAt] is null until the task is marked done -
 * it is the field the analytics/streak engine reads from, NOT [isCompleted]
 * alone, because we need to know *which day* a task was completed on.
 */
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String = "",
    val dueDate: LocalDate,
    val time: String? = null,
    val priority: Priority = Priority.MEDIUM,
    val project: ProjectType = ProjectType.PERSONAL,
    val isCompleted: Boolean = false,
    val completedAt: LocalDateTime? = null
)
