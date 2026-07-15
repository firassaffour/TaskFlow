package com.example.taskflow.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskflow.data.dao.TaskDAO
import com.example.taskflow.data.dao.UserProfileDAO
import com.example.taskflow.domain.models.Task
import com.example.taskflow.domain.models.UserProfile

@Database(
    entities = [Task::class, UserProfile::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract val taskDao : TaskDAO
    abstract val profileDao : UserProfileDAO
}