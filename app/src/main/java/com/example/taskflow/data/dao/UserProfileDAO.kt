package com.example.taskflow.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.taskflow.domain.models.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDAO {

    @Upsert
    suspend fun upsertProfile(profile: UserProfile)

    @Query("SELECT * FROM userprofile WHERE id = :id LIMIT 1")
    fun getProfile(id: Int = UserProfile.SINGLETON_ID): Flow<UserProfile?>
}
