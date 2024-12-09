package com.dicoding.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.utils.Result

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun getStoriesWithLocation() = liveData {
        _isLoading.value = true
        try {
            val stories = storyRepository.getStoriesWithLocation()
            emit(Result.Success(stories))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        } finally {
            _isLoading.value = false
        }
    }
}