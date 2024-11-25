package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.response.FileUploadResponse
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) = apiService.register(name, email, password)

    suspend fun saveUser(user: UserModel) {
        userPreference.saveUser(user)
    }

    suspend fun saveToken(token: String) {
        userPreference.saveToken(token)
    }

    fun getUser(): Flow<UserModel> {
        return userPreference.getUser()
    }

    suspend fun getStories(): List<ListStoryItem> {
        val response = apiService.getStories()
        return response.listStory
    }

    suspend fun uploadStory(description: RequestBody, photo: MultipartBody.Part): FileUploadResponse {
        return apiService.uploadStory(description, photo)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}