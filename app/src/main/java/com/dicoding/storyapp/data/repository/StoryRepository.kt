package com.dicoding.storyapp.data.repository

import android.util.Log
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.response.FileUploadResponse
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val userPreference: UserPreference
){
    private suspend fun getApiService(): ApiService {
        val token = userPreference.getUser().first().token
        return ApiConfig.getApiService(token)
    }

    suspend fun login(email: String, password: String) = getApiService().login(email, password)

    suspend fun register(name: String, email: String, password: String) = getApiService().register(name, email, password)

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
        val response = getApiService().getStories()
        return response.listStory
    }

    suspend fun uploadStory(description: RequestBody, photo: MultipartBody.Part): FileUploadResponse {
        val token = "Bearer ${userPreference.getUser().first().token}"
        Log.d("StoryRepository", "uploadStory: $token")
        return getApiService().uploadStory(token, description, photo)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference)
            }.also { instance = it }
    }
}