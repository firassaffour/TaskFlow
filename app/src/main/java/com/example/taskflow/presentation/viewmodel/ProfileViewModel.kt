package com.example.taskflow.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.DataStoreManager
import com.example.taskflow.domain.models.UserProfile
import com.example.taskflow.domain.repository.UserProfileRepository
import com.example.taskflow.domain.usecase.GetProfileUseCase
import com.example.taskflow.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {

    val profile = getProfileUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())


    fun updateName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            try {
                updateProfileUseCase(profile.value.copy(name = trimmed))
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "updateName error: ${e.message}")
            }
        }
    }

    fun updateImage(imagePath: String) {
        viewModelScope.launch {
            try {
                updateProfileUseCase(profile.value.copy(imagePath = imagePath))
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "updateImage error: ${e.message}")
            }
        }
    }

    fun setDarkMode(enabled: Boolean, context: Context) {
        viewModelScope.launch {
            try {
                val dataStoreManager = DataStoreManager(context)
                dataStoreManager.setDarkMode(enabled)
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "setDarkMode error: ${e.message}")
            }
        }
    }
}
