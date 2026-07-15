package com.example.taskflow.data.repository

import com.example.taskflow.data.dao.UserProfileDAO
import com.example.taskflow.domain.models.UserProfile
import com.example.taskflow.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepositoryImpl(
    private val dao: UserProfileDAO
) : UserProfileRepository {

    // Row may not exist yet on first launch - fall back to defaults so the
    // UI never has to null-check a profile that "just hasn't been saved yet".
    override fun getProfile(): Flow<UserProfile> =
        dao.getProfile().map { it ?: UserProfile() }

    override suspend fun updateProfile(profile: UserProfile) = dao.upsertProfile(profile)
}
