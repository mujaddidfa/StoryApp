package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.response.FileUploadResponse
import com.dicoding.storyapp.data.api.response.ListStoryItem
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun getStories(): List<ListStoryItem> {
        val response = apiService.getStories()
        return response.listStory
    }

    suspend fun uploadStory(description: RequestBody, photo: MultipartBody.Part): FileUploadResponse {
        return apiService.uploadStory(description, photo)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}