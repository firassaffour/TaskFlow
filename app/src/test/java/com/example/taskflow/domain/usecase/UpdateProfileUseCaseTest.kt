package com.example.taskflow.domain.usecase

import com.example.taskflow.data.repository.FakeProfileRepository
import com.example.taskflow.domain.models.UserProfile
import com.example.taskflow.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UpdateProfileUseCaseTest {

  private lateinit var updateProfileUseCase: UpdateProfileUseCase
  private lateinit var profileRepository: UserProfileRepository

@Before
 fun setUp() {
 profileRepository = mock()
 updateProfileUseCase = UpdateProfileUseCase(profileRepository)
}

  @Test
  fun `updateProfileUseCase updates profile`() = runBlocking {
   val profile = UserProfile(name = "Feras Saffour")
    updateProfileUseCase(profile)
    verify(profileRepository).updateProfile(profile)
  }
}