package com.dicoding.storyapp.view.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.utils.Result

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStoriesWithLocation() = liveData {
        emit(Result.Loading)
        try {
            val stories = storyRepository.getStoriesWithLocation()
            emit(Result.Success(stories))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}