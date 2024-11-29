package com.dicoding.storyapp.view.addstory

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.api.response.FileUploadResponse
import com.dicoding.storyapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    suspend fun uploadStory(description: RequestBody, photo: MultipartBody.Part): FileUploadResponse {
        return repository.uploadStory(description, photo)
    }
}