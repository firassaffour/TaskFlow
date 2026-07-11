package com.example.taskflow.presentation.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.domain.models.ProjectType
import com.example.taskflow.presentation.viewmodel.TaskViewModel
import com.example.taskflow.presentation.ui.components.CircularCompletionRing
import com.example.taskflow.presentation.ui.components.FlowCard
import com.example.taskflow.presentation.ui.components.SectionHeader
import com.example.taskflow.presentation.ui.components.TaskRow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()
    val completed = tasks.count { it.isCompleted }
    val progressPercent = if (tasks.isEmpty()) 0 else (completed * 100) / tasks.size

    val featured = tasks.firstOrNull { !it.isCompleted && it.time != null }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Good Morning,", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Alex", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        "You have ${tasks.count { !it.isCompleted }} tasks remaining for today.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                CircularCompletionRing(percent = progressPercent)
            }
        }

        if (featured != null) {
            item {
                FlowCard(modifier = Modifier.fillMaxWidth(), backgroundColor = MaterialTheme.colorScheme.primary) {
                    Text(
                        "PRIORITY: ${featured.priority.label.uppercase()}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(featured.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        featured.description.ifBlank { "Focus on this task first - it has the highest priority today." },
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { /* mark done */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("Complete Now", color = MaterialTheme.colorScheme.primary)
                        }
                        OutlinedButton(onClick = { /* reschedule */ }) {
                            Text("Reschedule", color = Color.White)
                        }
                    }
                }
            }
        }

        ProjectType.entries.forEach { project ->
            val projectTasks = tasks.filter { it.project == project }
            if (projectTasks.isNotEmpty()) {
                item {
                    FlowCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ) {
                        SectionHeader(project.label, projectTasks.size)
                        Spacer(Modifier.height(4.dp))
                        projectTasks.take(4).forEach { task ->
                            val currentTask = task.copy(isCompleted = !task.isCompleted)
                            TaskRow(task = task, onToggle = { taskViewModel.addTask(currentTask) })
                        }
                        if (projectTasks.size > 4) {
                            TextButton(onClick = { }) {
                                Text("+ Show ${projectTasks.size - 4} more $project tasks".lowercase())
                            }
                        }
                    }
                }
            }
        }
    }
}
