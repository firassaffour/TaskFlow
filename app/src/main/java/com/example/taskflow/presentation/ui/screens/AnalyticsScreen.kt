package com.example.taskflow.presentation.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.data.TaskRepository
import com.example.taskflow.presentation.viewmodel.AnalyticsEngine
import com.example.taskflow.domain.models.ProjectType
import com.example.taskflow.presentation.viewmodel.TaskViewModel
import com.example.taskflow.presentation.ui.components.FlowCard
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalyticsScreen(taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()
    val today = LocalDate.now()

    val totalCompleted = AnalyticsEngine.totalCompleted(tasks)
    val streak = AnalyticsEngine.currentStreak(TaskRepository.completionHistory, today)
    val tasksToday = tasks.filter { it.dueDate == today }
    val focusScore = AnalyticsEngine.focusScore(tasksToday, streak)
    val breakdown = AnalyticsEngine.taskBreakdown(tasks)
    val trend = AnalyticsEngine.weeklyTrend(tasks, today)
    val bestDay = AnalyticsEngine.mostProductiveDay(tasks)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Analytics", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                "Performance overview",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Total Completed",
                    value = "$totalCompleted",
                    color = MaterialTheme.colorScheme.primary
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Focus Score",
                    value = "$focusScore/100",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        item {
            FlowCard(modifier = Modifier.fillMaxWidth()) {
                Text("Current Streak", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$streak Days", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }

        item {
            FlowCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Weekly Productivity", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    val sign = if (trend.percentVsLastWeek >= 0) "+" else ""
                    Text(
                        "$sign${trend.percentVsLastWeek}% vs last week",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(12.dp))
                WeeklyBarChart(trend.perDay)
            }
        }

        item {
            FlowCard(modifier = Modifier.fillMaxWidth()) {
                Text("Task Breakdown", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                breakdown.forEach { share ->
                    val color = when (share.project) {
                        ProjectType.WORK -> MaterialTheme.colorScheme.primary
                        ProjectType.PERSONAL -> MaterialTheme.colorScheme.secondary
                        ProjectType.SHOPPING -> MaterialTheme.colorScheme.tertiary
                    }
                    BreakdownRow(share.project.label, share.count, share.percent, color)
                }
            }
        }

        if (bestDay != null) {
            item {
                FlowCard(modifier = Modifier.fillMaxWidth(), backgroundColor = MaterialTheme.colorScheme.primary) {
                    Text("Smart Insight", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "You're most productive on ${bestDay.getDisplayName(TextStyle.FULL, Locale.getDefault())}. " +
                            "Focus on your high-impact deep work that day for maximum efficiency.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, label: String, value: String, color: Color) {
    FlowCard(modifier = modifier) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = color)
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WeeklyBarChart(perDay: List<Pair<LocalDate, Int>>) {
    val maxValue = (perDay.maxOfOrNull { it.second } ?: 0).coerceAtLeast(1)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        perDay.forEach { (date, count) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .height((60 * (count.toFloat() / maxValue)).dp.coerceAtLeast(4.dp))
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(1),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BreakdownRow(label: String, count: Int, percent: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(Modifier.width(8.dp))
        Text("$label ($count)", modifier = Modifier.weight(1f), fontSize = 13.sp)
        Text("$percent%", fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}
