package com.example.taskflow.data.room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.taskflow.domain.models.Priority
import com.example.taskflow.domain.models.ProjectType
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room only knows how to persist primitives (String, Int, Long, Boolean...).
 * Every other type on [com.example.taskflow.domain.models.Task] -
 * LocalDate, LocalDateTime, Priority, ProjectType - needs an explicit
 * two-way converter here, or the app won't compile: Room's annotation
 * processor fails the build with "Cannot figure out how to save this
 * field into database" for each unconvertible column.
 *
 * Rule of thumb: one @TypeConverter pair (to-db / from-db) per type that
 * shows up as a field in an @Entity. Don't add converters for types
 * nothing uses yet - dead converters (or two converters that both claim
 * to produce the same output type) are exactly what caused the last
 * build to fail here.
 */
class Converters {

    // ---------- LocalDate <-> String (ISO-8601, e.g. "2026-07-10") ----------

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    // ---------- LocalDateTime <-> String (ISO-8601) ----------

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    // ---------- Priority enum <-> String ----------

    @TypeConverter
    fun fromPriority(priority: Priority?): String? = priority?.name

    @TypeConverter
    fun toPriority(value: String?): Priority? = value?.let { Priority.valueOf(it) }

    // ---------- ProjectType enum <-> String ----------

    @TypeConverter
    fun fromProjectType(project: ProjectType?): String? = project?.name

    @TypeConverter
    fun toProjectType(value: String?): ProjectType? = value?.let { ProjectType.valueOf(it) }
}
