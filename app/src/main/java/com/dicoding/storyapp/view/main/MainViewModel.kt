package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.data.repository.AuthRepository
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    storyRepository: StoryRepository
) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel> {
        return authRepository.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}