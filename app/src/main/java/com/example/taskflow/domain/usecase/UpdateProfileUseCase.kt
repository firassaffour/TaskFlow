package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.models.UserProfile
import com.example.taskflow.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val profileRepository: UserProfileRepository) {
    suspend operator fun invoke (profile: UserProfile) = profileRepository.updateProfile(profile)
}