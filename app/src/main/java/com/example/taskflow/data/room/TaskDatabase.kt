package com.example.taskflow.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskflow.data.dao.TaskDAO
import com.example.taskflow.domain.models.Task

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract val dao : TaskDAO
}