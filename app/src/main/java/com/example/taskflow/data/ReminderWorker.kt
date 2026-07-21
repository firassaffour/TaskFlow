package com.example.taskflow.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskflow.R
import com.example.taskflow.domain.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TaskRepository
) : CoroutineWorker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {

        val tasks = repository.getTasks().first()

        val hasPendingTask = tasks.any {
            !it.isCompleted && it.dueDate <= LocalDate.now()
        }

        if (hasPendingTask) {
            showNotification()
        }

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {

        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        val channel = NotificationChannel(
            "task_channel",
            "Tasks",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        manager.createNotificationChannel(channel)

        val notification =
            NotificationCompat.Builder(applicationContext, "task_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("TaskFlow")
                .setContentText("You still have unfinished tasks today.")
                .build()

        manager.notify(1, notification)
    }
}