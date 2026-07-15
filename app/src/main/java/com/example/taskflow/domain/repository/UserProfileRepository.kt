package com.example.taskflow.domain.repository

import com.example.taskflow.domain.models.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {

    /** Always emits a non-null profile - defaults are used until the user saves one. */
    fun getProfile(): Flow<UserProfile>

    suspend fun updateProfile(profile: UserProfile)
}
