package com.example.taskflow.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.taskflow.domain.models.Priority
import com.example.taskflow.domain.models.ProjectType
import com.example.taskflow.domain.models.Task
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * All the numbers on the Analytics tab come from these pure functions.
 * They take plain data in and return plain numbers out - no Compose,
 * no state, no side effects - so you can unit test every formula here
 * without touching the UI at all.
 */
object AnalyticsEngine {

    // ---------- Total completed ----------

    fun totalCompleted(tasks: List<Task>): Int =
        tasks.count { it.isCompleted }

    // ---------- Streak ----------

    /**
     * "Current streak" = number of consecutive days, walking backwards
     * from today, that have at least one entry in [completionDates].
     *
     * Rule we use: today doesn't have to be done yet for the streak to
     * still be "alive" (you might complete today's task at 11pm), but
     * yesterday, the day before, etc. must all be present or the streak
     * is broken.
     *
     * Example: completionDates = {today-1, today-2, today-3} and today
     * has nothing yet -> streak = 3 (counting from today-1 backwards).
     * If today-4 is missing, we stop there even if today-5 exists.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun currentStreak(completionDates: List<LocalDate>, today: LocalDate = LocalDate.now()): Int {
        val daySet = completionDates.toHashSet()
        var streak = 0
        var cursor = if (today in daySet) today else today.minusDays(1)

        // If today itself is done, count it, then keep walking back.
        // If today isn't done yet, we don't penalize - we just start
        // checking from yesterday.
        if (today in daySet) {
            streak++
            cursor = today.minusDays(1)
        }

        while (cursor in daySet) {
            streak++
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    // ---------- Focus score ----------

    /**
     * A 0-100 "how focused was today" score. This is a made-up but
     * reasonable formula you can tune:
     *
     *   50%  -> completion rate today (completed / due today)
     *   30%  -> weighted priority credit (HIGH tasks are worth more,
     *           so clearing your important work matters more than
     *           clearing easy filler tasks)
     *   20%  -> consistency bonus from the current streak, capped so
     *           a huge streak can't single-handedly max the score
     */
    fun focusScore(tasksToday: List<Task>, streak: Int): Int {
        if (tasksToday.isEmpty()) return 0

        val completionRate = tasksToday.count { it.isCompleted }.toDouble() / tasksToday.size

        fun weight(p: Priority) = when (p) {
            Priority.HIGH -> 3.0
            Priority.MEDIUM -> 2.0
            Priority.LOW -> 1.0
        }
        val maxWeight = tasksToday.sumOf { weight(it.priority) }
        val earnedWeight = tasksToday.filter { it.isCompleted }.sumOf { weight(it.priority) }
        val weightedRate = if (maxWeight > 0) earnedWeight / maxWeight else 0.0

        val streakBonus = (streak.coerceAtMost(10) / 10.0) // 0..1

        val score = (completionRate * 50) + (weightedRate * 30) + (streakBonus * 20)
        return score.coerceIn(0.0, 100.0).toInt()
    }

    // ---------- Task breakdown (for the donut chart) ----------

    data class ProjectShare(val project: ProjectType, val count: Int, val percent: Int)

    fun taskBreakdown(tasks: List<Task>): List<ProjectShare> {
        val total = tasks.size
        if (total == 0) return emptyList()
        return ProjectType.entries.mapNotNull { project ->
            val count = tasks.count { it.project == project }
            if (count == 0) return@mapNotNull null
            ProjectShare(project, count, (count * 100) / total)
        }
    }

    // ---------- Weekly productivity trend ----------

    /**
     * Tasks completed per day for the last 7 days (oldest first), plus
     * the percent change vs. the 7 days before that - this is the
     * "+12% vs last week" you see on the Analytics screen.
     */
    data class WeeklyTrend(val perDay: List<Pair<LocalDate, Int>>, val percentVsLastWeek: Int)

    @RequiresApi(Build.VERSION_CODES.O)
    fun weeklyTrend(tasks: List<Task>, today: LocalDate = LocalDate.now()): WeeklyTrend {
        val completedDates = tasks.filter { it.isCompleted }
            .mapNotNull { it.completedAt?.toLocalDate() }

        fun countOn(day: LocalDate) = completedDates.count { it == day }

        val last7 = (6 downTo 0).map { offset ->
            val day = today.minusDays(offset.toLong())
            day to countOn(day)
        }
        val thisWeekTotal = last7.sumOf { it.second }

        val prev7Total = (13 downTo 7).sumOf { offset ->
            countOn(today.minusDays(offset.toLong()))
        }

        val percentChange = when {
            prev7Total == 0 && thisWeekTotal == 0 -> 0
            prev7Total == 0 -> 100
            else -> (((thisWeekTotal - prev7Total).toDouble() / prev7Total) * 100).toInt()
        }

        return WeeklyTrend(last7, percentChange)
    }

    // ---------- Smart insight ----------

    /**
     * Finds the weekday the user historically completes the most tasks
     * on, purely from the completedAt timestamps - this is what powers
     * the "You're most productive on Tuesday mornings" card.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun mostProductiveDay(tasks: List<Task>): DayOfWeek? {
        val completed = tasks.mapNotNull { it.completedAt }
        if (completed.isEmpty()) return null
        return completed
            .groupingBy { it.dayOfWeek }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun daysBetween(a: LocalDate, b: LocalDate): Long = ChronoUnit.DAYS.between(a, b)
}
