package com.example.taskflow.presentation.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.taskflow.domain.models.Priority
import com.example.taskflow.domain.models.ProjectType
import java.time.LocalDate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.testTag
import com.example.taskflow.domain.models.Task
import com.example.taskflow.presentation.viewmodel.TaskViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTaskScreen(onTaskCreated: () -> Unit, navController: NavHostController, taskViewModel: TaskViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var project by remember { mutableStateOf(ProjectType.PERSONAL) }
    var dueDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {navController.popBackStack()},
                modifier = Modifier.offset(x = (-10).dp)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "back")
            }

            Spacer(Modifier.width(10.dp))

            Text("New Task", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(Modifier.height(20.dp))

        Text("TASK TITLE", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("What needs to be done?") },
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp).testTag("TITLE_FIELD")
        )

        Spacer(Modifier.height(16.dp))
        Text("DESCRIPTION", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Add more details about this task...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(top = 6.dp)
                .testTag("DESCRIPTION_FIELD")
        )

        Spacer(Modifier.height(16.dp))
        Text("DUE DATE", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChoiceChip("Today", dueDate == LocalDate.now()) { dueDate = LocalDate.now() }
            ChoiceChip("Tomorrow", dueDate == LocalDate.now().plusDays(1)) { dueDate = LocalDate.now().plusDays(1) }
            ChoiceChip("Pick Date", false) { /* open date picker */ }
        }

        Spacer(Modifier.height(16.dp))
        Text("PRIORITY", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Priority.entries.forEach { p ->
                ChoiceChip(p.label, priority == p, filled = true) { priority = p }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("PROJECT", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProjectType.entries.forEach { pr ->
                ChoiceChip(pr.label, project == pr, filled = true) { project = pr }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    val task = Task(
                        id = 0,
                        title = title,
                        description = description,
                        dueDate = dueDate,
                        priority = priority,
                        project = project,
                        isCompleted = false,
                    )
                    taskViewModel.addTask(task)
                    onTaskCreated()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("ADD_TASK_BUTTON"),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("+  Create Task", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ChoiceChip(
    label: String,
    selected: Boolean,
    filled: Boolean = false,
    onClick: () -> Unit
) {
    val bg = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val fg = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag(label)
    ) {
        Text(label, color = fg, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
