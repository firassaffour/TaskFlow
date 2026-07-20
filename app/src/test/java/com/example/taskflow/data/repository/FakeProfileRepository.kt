package com.example.taskflow.data.repository

import com.example.taskflow.domain.models.UserProfile
import com.example.taskflow.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProfileRepository : UserProfileRepository {

    var profile = UserProfile(name = "John Doe")

    override fun getProfile(): Flow<UserProfile> {
        return flow { emit(profile) }
    }

    override suspend fun updateProfile(profile: UserProfile) {
        this.profile = profile
    }
}