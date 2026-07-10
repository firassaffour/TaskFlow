package com.example.taskflow.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import com.example.taskflow.domain.models.Priority
import com.example.taskflow.domain.models.ProjectType
import com.example.taskflow.domain.models.Task
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Single source of truth for tasks, kept in memory for this demo.
 * Swap this for a Room DAO / ViewModel + Repository later -
 * every screen only ever talks to this object, never to raw data.
 */
object TaskRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val tasks = mutableStateListOf(
        Task(
            id = 1,
            title = "Review Design System Specs",
            dueDate = today,
            time = "2:00 PM",
            priority = Priority.MEDIUM,
            project = ProjectType.WORK
        ),
        Task(
            id = 2,
            title = "Client Call: TechCorp",
            dueDate = today,
            time = "4:30 PM",
            priority = Priority.MEDIUM,
            project = ProjectType.WORK
        ),
        Task(
            id = 3,
            title = "Draft Weekly Report",
            dueDate = today.minusDays(1),
            priority = Priority.LOW,
            project = ProjectType.WORK,
            isCompleted = true,
            completedAt = LocalDateTime.now().minusDays(1)
        ),
        Task(
            id = 4,
            title = "Gym Session",
            dueDate = today,
            time = "6:00 PM",
            priority = Priority.LOW,
            project = ProjectType.PERSONAL
        ),
        Task(
            id = 5,
            title = "Buy Groceries",
            dueDate = today,
            time = "7:30 PM",
            priority = Priority.MEDIUM,
            project = ProjectType.PERSONAL
        ),
        Task(
            id = 6,
            title = "Finalize Project Proposal",
            dueDate = today,
            time = "09:00 AM",
            priority = Priority.HIGH,
            project = ProjectType.WORK
        ),
        Task(
            id = 7,
            title = "Team Standup",
            dueDate = today,
            time = "10:30 AM",
            priority = Priority.LOW,
            project = ProjectType.WORK,
            isCompleted = true,
            completedAt = LocalDateTime.now()
        ),
        Task(
            id = 8,
            title = "Client Feedback Implementation",
            dueDate = today,
            time = "02:00 PM",
            priority = Priority.HIGH,
            project = ProjectType.WORK
        )
    )

    // Fake historical completion log so the streak/analytics engine has
    // more than one real day of data to chew on. In a real app this would
    // just be `tasks.filter { it.completedAt != null }` across all history.
    @RequiresApi(Build.VERSION_CODES.O)
    val completionHistory = mutableStateListOf(
        today.minusDays(1),
        today.minusDays(2),
        today.minusDays(3),
        today.minusDays(4),
        today.minusDays(5)
        // today.minusDays(6) intentionally missing -> shows streak breaking logic
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private var nextId = tasks.size + 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(
        title: String,
        description: String,
        dueDate: LocalDate,
        priority: Priority,
        project: ProjectType
    ) {
        tasks.add(
            Task(
                id = nextId++,
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority,
                project = project
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleComplete(taskId: Int) {
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index == -1) return
        val current = tasks[index]
        val nowCompleted = !current.isCompleted
        tasks[index] = current.copy(
            isCompleted = nowCompleted,
            completedAt = if (nowCompleted) LocalDateTime.now() else null
        )
        if (nowCompleted && today !in completionHistory) {
            completionHistory.add(today)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun tasksFor(project: ProjectType, tasks : List<Task>) = tasks.filter { it.project == project }
}
