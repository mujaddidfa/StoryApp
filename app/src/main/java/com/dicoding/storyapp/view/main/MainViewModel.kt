package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.data.repository.AuthRepository
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.utils.Result
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return authRepository.getUser().asLiveData()
    }

    fun getStories(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val stories = storyRepository.getStories()
            emit(Result.Success(stories))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}