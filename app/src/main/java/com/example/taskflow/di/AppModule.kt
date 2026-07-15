package com.example.taskflow.di

import android.app.Application
import androidx.room.Room
import com.example.taskflow.data.repository.TaskRepositoryImpl
import com.example.taskflow.data.repository.UserProfileRepositoryImpl
import com.example.taskflow.data.room.TaskDatabase
import com.example.taskflow.domain.repository.TaskRepository
import com.example.taskflow.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "task_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db.taskDao)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(db: TaskDatabase): UserProfileRepository {
        return UserProfileRepositoryImpl(db.profileDao)
    }
}