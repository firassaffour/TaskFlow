package com.example.taskflow.domain.usecase

import com.example.taskflow.domain.repository.UserProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val profileRepository: UserProfileRepository) {
    operator fun invoke() = profileRepository.getProfile()
}